package io.github.NEVERMAIN.gateway.core.socket.handlers;

import io.github.NEVERMAIN.gateway.core.ratelimit.RateLimiterLuaScripts;
import io.github.NEVERMAIN.gateway.core.ratelimit.RedisClientFactory;
import io.github.NEVERMAIN.gateway.core.session.Configuration;
import io.github.NEVERMAIN.gateway.core.socket.BaseHandler;
import io.github.NEVERMAIN.gateway.core.socket.agreement.AgreementConstants;
import io.github.NEVERMAIN.gateway.core.socket.agreement.GatewayResultMessage;
import io.github.NEVERMAIN.gateway.core.socket.agreement.RequestParser;
import io.github.NEVERMAIN.gateway.core.socket.agreement.ResponseParser;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.ScriptOutputType;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@ChannelHandler.Sharable
public class ApiRateLimitingHandler extends BaseHandler<FullHttpRequest> {

    private static final Logger logger = LoggerFactory.getLogger(ApiRateLimitingHandler.class);

    private Configuration configuration;

    private final RedisAsyncCommands<String, String> asyncCommands;

    public ApiRateLimitingHandler(Configuration configuration) {
        this.configuration = configuration;
        RedisClientFactory redisClientFactory = configuration.getRedisClientFactory();
        asyncCommands = redisClientFactory.getAsyncCommands();
    }

    @Override
    protected void session(ChannelHandlerContext ctx, Channel channel, FullHttpRequest request) {

        logger.info("网关接收请求【限流】 uri:{} method:{}", request.uri(), request.method());
        String traceId = channel.attr(AgreementConstants.TRACE_ID_KEY).get();
        // 1.定义限流规则
        RequestParser requestParser = new RequestParser(request);
        String uri = requestParser.getUri();

        String rateLimitKey = "rate_limit:" + uri;
        String timeWindow = configuration.getTimeWindow();
        String maxRequests = configuration.getMaxRequests();

        try {
            // 2. 同步执行限流检查
            Long result = (Long) asyncCommands.eval(
                    RateLimiterLuaScripts.SLIDING_WINDOW_SCRIPT,
                    ScriptOutputType.INTEGER,
                    new String[]{rateLimitKey},
                    timeWindow,
                    maxRequests
            ).get(5, TimeUnit.SECONDS);

            if (result == 1L) {
                // 允许访问
                logger.info("key:{} 访问被允许", rateLimitKey);
                request.retain();
                ctx.fireChannelRead(request);
            } else {
                // 拒绝访问
                logger.info("key:{} 访问被拒绝(超出限额)", rateLimitKey);
                DefaultFullHttpResponse response = new ResponseParser().parse(GatewayResultMessage.buildError(
                        AgreementConstants.ResponseCode._403.getCode(),
                        "Too Many Requests,Rate limit exceeded",traceId
                ));
                ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
            }
        } catch (Exception e) {
            // Redis 出错，为了安全起见拒绝请求
            logger.error("限流检查时 Redis 出错:{}", e.getMessage());
            DefaultFullHttpResponse response = new ResponseParser().parse(GatewayResultMessage.buildError(
                    AgreementConstants.ResponseCode._500.getCode(),
                    "限流检查时 Redis 出错",traceId
            ));
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        }
    }
}

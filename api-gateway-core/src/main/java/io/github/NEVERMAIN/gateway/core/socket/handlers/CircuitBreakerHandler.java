package io.github.NEVERMAIN.gateway.core.socket.handlers;

import io.github.NEVERMAIN.gateway.core.circuitbreaker.BreakerContext;
import io.github.NEVERMAIN.gateway.core.circuitbreaker.CircuitBreakerFactory;
import io.github.NEVERMAIN.gateway.core.session.Configuration;
import io.github.NEVERMAIN.gateway.core.socket.BaseHandler;
import io.github.NEVERMAIN.gateway.core.socket.agreement.AgreementConstants;
import io.github.NEVERMAIN.gateway.core.socket.agreement.GatewayResultMessage;
import io.github.NEVERMAIN.gateway.core.socket.agreement.RequestParser;
import io.github.NEVERMAIN.gateway.core.socket.agreement.ResponseParser;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.netty.channel.*;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.util.ReferenceCountUtil;

import java.util.concurrent.TimeUnit;

/**
 * Netty Pipeline 统一熔断处理器（入站 + 出站）
 * 入站：判断熔断是否放行；不放行则快速兜底
 * 出站：根据 write() 的 Promise 成功/失败回调熔断器
 */
public class CircuitBreakerHandler extends ChannelDuplexHandler {

    private final Configuration configuration;

    public CircuitBreakerHandler(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        String traceId = ctx.channel().attr(AgreementConstants.TRACE_ID_KEY).get();

        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            String apiKey = extractApiKey(request);

            CircuitBreakerFactory circuitBreakerFactory = configuration.getCircuitBreakerFactory();
            CircuitBreaker circuitBreader = circuitBreakerFactory.getCircuitBreader(apiKey);

            // 熔断器不允许,快速兜底释放请求
            if (!circuitBreader.tryAcquirePermission()) {
                ReferenceCountUtil.release(request);
                sendFallback(ctx, AgreementConstants.ResponseCode._503.getCode(),
                        "Service unavailable, blocked by circuit breaker", traceId);
                return;
            }

            long start = System.nanoTime();
            ctx.channel().attr(AgreementConstants.BREAKER_CONTEXT).set(new BreakerContext(circuitBreader, apiKey, start));

            // 继续传播
            super.channelRead(ctx, msg);
            return;
        }
        super.channelRead(ctx, msg);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        // 仅在最终写回 HTTP 响应时与入站请求成对打点
        if (msg instanceof HttpResponse) {

            BreakerContext breakerContext = ctx.channel().attr(AgreementConstants.BREAKER_CONTEXT).get();
            if (breakerContext != null) {
                promise.addListener(future -> {
                    long duration = System.nanoTime() - breakerContext.getStartNano();
                    CircuitBreaker breaker = breakerContext.getBreaker();

                    if (future.isSuccess()) {
                        breaker.onSuccess(duration, TimeUnit.NANOSECONDS);
                    } else {
                        breaker.onError(duration, TimeUnit.NANOSECONDS, future.cause());
                    }
                });
            }
        }
        super.write(ctx, msg, promise);
    }

    private String extractApiKey(FullHttpRequest request) {
        RequestParser requestParser = new RequestParser(request);
        return requestParser.getUri();
    }

    private void sendFallback(ChannelHandlerContext ctx, String code, String message, String traceId) {
        DefaultFullHttpResponse response =
                new ResponseParser().parse(GatewayResultMessage.buildError(code, message, traceId));
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

}

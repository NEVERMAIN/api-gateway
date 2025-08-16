package io.github.NEVERMAIN.gateway.core.socket.handlers;

import io.github.NEVERMAIN.gateway.core.bind.IGenericReference;
import io.github.NEVERMAIN.gateway.core.executor.result.SessionResult;
import io.github.NEVERMAIN.gateway.core.metrics.MetricsCollector;
import io.github.NEVERMAIN.gateway.core.session.Configuration;
import io.github.NEVERMAIN.gateway.core.session.GatewaySession;
import io.github.NEVERMAIN.gateway.core.session.defaults.DefaultGatewaySessionFactory;
import io.github.NEVERMAIN.gateway.core.socket.BaseHandler;
import io.github.NEVERMAIN.gateway.core.socket.agreement.AgreementConstants;
import io.github.NEVERMAIN.gateway.core.socket.agreement.GatewayResultMessage;
import io.github.NEVERMAIN.gateway.core.socket.agreement.RequestParser;
import io.github.NEVERMAIN.gateway.core.socket.agreement.ResponseParser;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.CompletionStage;

/**
 * @description 请求数据处理
 */
public class ProtocolDataHandler extends BaseHandler<FullHttpRequest> {

    private static final Logger log = LoggerFactory.getLogger(ProtocolDataHandler.class);
    private final DefaultGatewaySessionFactory gatewaySessionFactory;
    private final Configuration configuration;
    private final MetricsCollector metricsCollector;

    public ProtocolDataHandler(DefaultGatewaySessionFactory gatewaySessionFactory,
                               Configuration configuration) {
        this.gatewaySessionFactory = gatewaySessionFactory;
        this.configuration = configuration;
        this.metricsCollector = configuration.getMetricsCollector();
    }

    @Override
    protected void session(ChannelHandlerContext ctx, Channel channel, FullHttpRequest request) {
        log.info("网关接收请求【消息】 uri:{} method:{}", request.uri(), request.method());
        long startTime = System.nanoTime();
        // 1.解析请求参数
        RequestParser requestParser = new RequestParser(request);
        String uri = requestParser.getUri();
        if (uri == null) return;
        Map<String, Object> args = requestParser.parse();
        String traceId = channel.attr(AgreementConstants.TRACE_ID_KEY).get();
        try {
            // 2.调用会话服务
            GatewaySession gatewaySession = gatewaySessionFactory.openSession(uri);
            IGenericReference reference = gatewaySession.getMapper();
            CompletionStage<Object> future = reference.$invokeAsync(args);

            // 3.异步处理结果并回写
            future.whenComplete((result, throwable) -> {
                ctx.executor().execute(() -> {
                    DefaultFullHttpResponse response = null;
                    if (throwable != null) {
                        GatewayResultMessage gatewayResultMessage = GatewayResultMessage.buildError(
                                AgreementConstants.ResponseCode._500.getCode(),
                                "网关协议调用失败!!!" + throwable.getMessage(),
                                traceId);
                        response = new ResponseParser().parse(gatewayResultMessage, HttpResponseStatus.SERVICE_UNAVAILABLE);
                    } else {
                        response = new ResponseParser().parse(GatewayResultMessage.buildSuccess(result, traceId));
                    }

                    // 统计监控信息
                    long duration = System.nanoTime() - startTime;
                    metricsCollector.recordRequest(uri);
                    metricsCollector.recordLatency(uri, duration);
                    metricsCollector.recordStatus(uri, response.status().code());

                    // 5.写回客户端
                    channel.writeAndFlush(response)
                            .addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                });

            });


        } catch (Exception e) {
            // 异常场景(同步失败)
            GatewayResultMessage gatewayResultMessage = GatewayResultMessage.buildError(
                    AgreementConstants.ResponseCode._502.getCode(), "网关协议调用失败！" + e.getMessage(), traceId);
            DefaultFullHttpResponse response = new ResponseParser().parse(gatewayResultMessage, HttpResponseStatus.SERVICE_UNAVAILABLE);

            long duration = System.nanoTime() - startTime;
            metricsCollector.recordRequest(uri);
            metricsCollector.recordLatency(uri, duration);
            metricsCollector.recordStatus(uri, response.status().code());

            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        }

    }
}

package io.github.NEVERMAIN.gateway.core.socket.handlers;

import io.github.NEVERMAIN.gateway.core.metrics.MetricsCollector;
import io.github.NEVERMAIN.gateway.core.session.Configuration;
import io.github.NEVERMAIN.gateway.core.socket.BaseHandler;
import io.github.NEVERMAIN.gateway.core.socket.agreement.AgreementConstants;
import io.github.NEVERMAIN.gateway.core.socket.agreement.GatewayResultMessage;
import io.github.NEVERMAIN.gateway.core.socket.agreement.ResponseParser;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;

public class MetricsHandler extends BaseHandler<FullHttpRequest> {

    private final Configuration configuration;

    private final MetricsCollector metricsCollector;

    public MetricsHandler(Configuration configuration) {
        this.configuration = configuration;
        this.metricsCollector = configuration.getMetricsCollector();
    }

    @Override
    protected void session(ChannelHandlerContext ctx, Channel channel, FullHttpRequest request) {
        if (request.uri().equals("/metrics")) {
            String metrics = metricsCollector.exportMetrics();

            String traceId = channel.attr(AgreementConstants.TRACE_ID_KEY).get();
            GatewayResultMessage gatewayResultMessage = GatewayResultMessage.buildSuccess(metrics, traceId);
            DefaultFullHttpResponse response = new ResponseParser().parse(gatewayResultMessage);
            ctx.writeAndFlush(response);
        } else {
            ctx.fireChannelRead(request.retain());
        }
    }
}

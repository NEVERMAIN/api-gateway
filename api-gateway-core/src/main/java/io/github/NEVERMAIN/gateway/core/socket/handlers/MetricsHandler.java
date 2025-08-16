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
import io.netty.handler.codec.http.HttpHeaderNames;

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

            DefaultFullHttpResponse response = new ResponseParser().parse(metrics);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; version=0.0.4; charset=utf-8");
            ctx.writeAndFlush(response);
        } else {
            ctx.fireChannelRead(request.retain());
        }
    }
}

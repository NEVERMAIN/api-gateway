package io.github.NEVERMAIN.gateway.core.socket;

import io.github.NEVERMAIN.gateway.core.session.Configuration;
import io.github.NEVERMAIN.gateway.core.session.defaults.DefaultGatewaySessionFactory;
import io.github.NEVERMAIN.gateway.core.socket.handlers.*;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * @description 网络协议处理
 */
public class GatewayChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final DefaultGatewaySessionFactory gatewaySessionFactory;

    private final Configuration configuration;

    public GatewayChannelInitializer(DefaultGatewaySessionFactory gatewaySessionFactory, Configuration configuration) {
        this.gatewaySessionFactory = gatewaySessionFactory;
        this.configuration = configuration;
    }

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(new HttpRequestDecoder());
        pipeline.addLast(new HttpResponseEncoder());
        pipeline.addLast(new HttpObjectAggregator(1024 * 1024));
        pipeline.addLast(new TraceContextHandler());
        pipeline.addLast(new MetricsHandler(configuration));
        pipeline.addLast(new GatewayServerHandler(configuration));
        pipeline.addLast(new AuthorizationHandler(configuration));
        pipeline.addLast(new ApiRateLimitingHandler(configuration));
        pipeline.addLast(new CircuitBreakerHandler(configuration));
        pipeline.addLast(new ProtocolDataHandler(gatewaySessionFactory, configuration));
    }
}

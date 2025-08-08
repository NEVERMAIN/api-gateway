package io.github.NEVERMAIN.gateway.core.socket;

import io.github.NEVERMAIN.gateway.core.session.Configuration;
import io.github.NEVERMAIN.gateway.core.session.defaults.DefaultGatewaySessionFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * 网关会话服务
 */
public class GatewaySocketServer implements Callable<Channel> {

    private static final Logger log = LoggerFactory.getLogger(GatewaySocketServer.class);

    private final DefaultGatewaySessionFactory gatewaySessionFactory;

    private final Configuration configuration;

    private  EventLoopGroup parentGroup;
    private  EventLoopGroup childGroup;
    private Channel channel;

    public GatewaySocketServer(DefaultGatewaySessionFactory gatewaySessionFactory, Configuration configuration) {
        this.gatewaySessionFactory = gatewaySessionFactory;
        this.configuration = configuration;
        this.initEventLoopGroup();
    }

    private void initEventLoopGroup(){
        parentGroup = new NioEventLoopGroup(configuration.getBossNThreads());
        childGroup = new NioEventLoopGroup(configuration.getWorkNThreads());
    }


    @Override
    public Channel call() throws Exception {

        ChannelFuture channelFuture = null;
        try{

            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(parentGroup,childGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,1024)
                    .childHandler(new GatewayChannelInitializer(gatewaySessionFactory,configuration));

            // Docker 容器部署会自动分配 IP,所以这里只需要设定端口即可。
            channelFuture = serverBootstrap.bind(configuration.getPort()).syncUninterruptibly();
            this.channel = channelFuture.channel();

        }catch (Exception e){
            log.error("socket server start error. ",e);
        }finally {
            if(null != channelFuture && channelFuture.isSuccess()){
                log.info("socket server start done.");
            }else {
                log.error("socket server start error.");
            }
        }

        return channel;

    }
}

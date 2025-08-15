package io.github.NEVERMAIN.gateway.core.socket.handlers;

import io.github.NEVERMAIN.gateway.core.socket.agreement.AgreementConstants;
import io.github.NEVERMAIN.gateway.core.socket.agreement.GatewayResultMessage;
import io.github.NEVERMAIN.gateway.core.socket.agreement.ResponseParser;
import io.netty.channel.*;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.Random;

public class RandomRouteHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private static final Random RANDOM = new Random();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        String uri = request.uri();
        System.out.println("RouteHandler: forwarding request to backend, uri=" + uri);

        // 模拟后端调用耗时（100ms ~ 500ms）
        Thread.sleep(100 + RANDOM.nextInt(400));

        // 模拟 50% 成功，50% 失败
        boolean success = RANDOM.nextBoolean();

        if (success) {
            ChannelPromise channelPromise = ctx.channel().newPromise();
            DefaultFullHttpResponse response = new ResponseParser().parse(GatewayResultMessage.buildSuccess("success"));
            ctx.writeAndFlush(response);
            channelPromise.setSuccess();
        } else {
            GatewayResultMessage gatewayResultMessage = GatewayResultMessage.buildError(
                    AgreementConstants.ResponseCode._502.getCode(),
                    "网关协议调用失败！");
            DefaultFullHttpResponse response = new ResponseParser().parse(gatewayResultMessage, HttpResponseStatus.SERVICE_UNAVAILABLE);
            ctx.writeAndFlush(response);
        }
    }
}

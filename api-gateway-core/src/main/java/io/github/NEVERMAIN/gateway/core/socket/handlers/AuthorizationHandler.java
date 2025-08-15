package io.github.NEVERMAIN.gateway.core.socket.handlers;

import io.github.NEVERMAIN.gateway.core.mapping.HttpStatement;
import io.github.NEVERMAIN.gateway.core.session.Configuration;
import io.github.NEVERMAIN.gateway.core.socket.BaseHandler;
import io.github.NEVERMAIN.gateway.core.socket.agreement.AgreementConstants;
import io.github.NEVERMAIN.gateway.core.socket.agreement.GatewayResultMessage;
import io.github.NEVERMAIN.gateway.core.socket.agreement.ResponseParser;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description 认证处理器
 */
public class AuthorizationHandler extends BaseHandler<FullHttpRequest> {

    private static final Logger log = LoggerFactory.getLogger(AuthorizationHandler.class);
    private final Configuration configuration;

    public AuthorizationHandler(Configuration configuration){
        this.configuration = configuration;
    }

    @Override
    protected void session(ChannelHandlerContext ctx, Channel channel, FullHttpRequest request) {
        log.info("网关接收请求【鉴权】 uri:{} method:{}",request.uri(),request.method());

        try{

            HttpStatement httpStatement = channel.attr(AgreementConstants.HTTP_STATEMENT).get();
            if(httpStatement.isAuth()){
                try{
                    // 1.鉴权信息
                    String uId = request.headers().get("uId");
                    String token = request.headers().get("token");
                    // 2.鉴权判断
                    if(null == token || "".equals( token)){
                        // 返回异常结果
                        GatewayResultMessage gatewayResultMessage = GatewayResultMessage.buildError(AgreementConstants.ResponseCode._400.getCode(), "对不起，你的 token 不合法！");
                        DefaultFullHttpResponse response = new ResponseParser().parse(gatewayResultMessage, HttpResponseStatus.BAD_REQUEST);
                        channel.writeAndFlush(response);
                    }

                    // 3.鉴权处理: Shiro+JWT
                    boolean status = configuration.authValidate(uId, token);

                    // 4.鉴权成功则直接放行
                    if(status){
                        request.retain();
                        ctx.fireChannelRead(request);
                    }else{
                        // 5.鉴权失败则返回错误信息
                        GatewayResultMessage gatewayResultMessage = GatewayResultMessage.buildError(AgreementConstants.ResponseCode._403.getCode(), "对不起，你无权访问此接口！");
                        DefaultFullHttpResponse response = new ResponseParser().parse(gatewayResultMessage, HttpResponseStatus.UNAUTHORIZED);
                        channel.writeAndFlush(response);
                    }

                }catch (Exception e){
                    DefaultFullHttpResponse response = new ResponseParser().parse(GatewayResultMessage.buildError(AgreementConstants.ResponseCode._403.getCode(), "对不起，你的鉴权不合法！"));
                    channel.writeAndFlush(response);
                }

            }else{
                // 不鉴权放行
                request.retain();
                ctx.fireChannelRead(request);
            }

        }catch (Exception e){
            // 4.封装返回结果
            DefaultFullHttpResponse response = new ResponseParser().parse(GatewayResultMessage.buildError(AgreementConstants.ResponseCode._500.getCode()
                    , "网关协议调用失败!!! " + e.getMessage()));
            channel.writeAndFlush(response);
        }
    }
}

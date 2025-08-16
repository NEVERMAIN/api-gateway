package io.github.NEVERMAIN.gateway.core.socket.agreement;

import com.alibaba.fastjson2.JSON;
import io.netty.handler.codec.http.*;
import org.slf4j.MDC;

/**
 * @description 返回结果解析器
 */
public class ResponseParser {

    public DefaultFullHttpResponse parse(Object result, HttpResponseStatus status) {

        // 返回信息控制
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                status
        );

        // 4.设置回写数据
        response.content().writeBytes(JSON.toJSONBytes(result));

        // 头部信息设置
        HttpHeaders headers = response.headers();
        // 返回内容类型
        headers.set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON + "; charset=UTF-8");
        // 响应体长度
        headers.setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        // 配置持久化连接
        headers.set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);

        // traceId 回写
        if(result instanceof GatewayResultMessage){
            GatewayResultMessage gatewayResultMessage = (GatewayResultMessage) result;
            String traceId = gatewayResultMessage.getTraceId();
            if (traceId == null) {
                // 兜底从 MDC 取（推荐由上游统一设置到 response 对象）
                traceId = MDC.get("traceId");
            }
            if(traceId != null){
                headers.add("X-Trace-Id", traceId);
            }
        }
        // 配置跨域
        headers.add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        headers.add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_HEADERS, "*");
        headers.add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PUT, DELETE");
        headers.add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");

        return response;

    }

    public DefaultFullHttpResponse parse(Object result) {
        return parse(result, HttpResponseStatus.OK);
    }


}

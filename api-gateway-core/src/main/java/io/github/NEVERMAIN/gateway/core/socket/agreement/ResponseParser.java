package io.github.NEVERMAIN.gateway.core.socket.agreement;

import com.alibaba.fastjson2.JSON;
import io.netty.handler.codec.http.*;

/**
 * @description 返回结果解析器
 */
public class ResponseParser {

    public DefaultFullHttpResponse parse(Object result){
        // 返回信息控制
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK
        );

        // 4.设置回写数据
        response.content().writeBytes(JSON.toJSONBytes(result));

        // 头部信息设置
        HttpHeaders headers = response.headers();
        // 返回内容类型
        headers.add(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON + "; charset=UTF-8");
        // 响应体长度
        headers.add(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        // 配置持久化连接
        headers.add(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        // 配置跨域
        headers.add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        headers.add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_HEADERS, "*");
        headers.add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PUT, DELETE");
        headers.add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");

        return response;
    }



}

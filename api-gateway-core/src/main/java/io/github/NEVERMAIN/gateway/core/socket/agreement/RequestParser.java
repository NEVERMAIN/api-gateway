package io.github.NEVERMAIN.gateway.core.socket.agreement;

import com.alibaba.fastjson2.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 请求解析器,用于解析 HTTP 请求,GET/POST
 */
public class RequestParser {

    private final FullHttpRequest request;

    public RequestParser(FullHttpRequest request) {
        this.request = request;
    }

    public String getUri(){
        String uri = request.uri();
        int idx = uri.indexOf("?");
        uri = idx > 0 ? uri.substring(0, idx) : uri;
        if (uri.equals("/favicon.ico")) return null;
        return uri;
    }


    public Map<String, Object> parse() {

        // 1.获取请求的类型
        HttpMethod method = request.method();
        if (HttpMethod.GET.equals(method)) {

            HashMap<String, Object> parameterMap = new HashMap<>();
            QueryStringDecoder decoder = new QueryStringDecoder(request.uri());
            decoder.parameters().forEach((key, value) -> parameterMap.put(key, value.get(0)));
            return parameterMap;

        } else if (HttpMethod.POST.equals(method)) {
            // 1.获取 Content-Type
            String contentType = getContentType();
            switch (contentType) {

                case "multipart/form-data":
                    HashMap<String, Object> parameterMap = new HashMap<>();
                    HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(request);
                    decoder.offer(request);
                    decoder.getBodyHttpDatas().forEach(data -> {
                        Attribute attr = (Attribute) data;
                        try {
                            parameterMap.put(attr.getName(), attr.getValue());
                        } catch (Exception e) {

                        }
                    });
                    return parameterMap;

                case "application/json":

                    ByteBuf byteBuf = request.content().copy();
                    if (byteBuf.isReadable()) {
                        String content = byteBuf.toString(StandardCharsets.UTF_8);
                        return JSON.parseObject(content);
                    }
                    break;

                default:
                    throw new RuntimeException("未实现的 Content-Type：" + contentType);
            }
        }
        throw new RuntimeException("未实现的请求类型 HttpMethod：" + method);
    }


    private String getContentType() {
        Optional<Map.Entry<String, String>> header = request.headers().entries().stream().filter(
                entity -> entity.getKey().equals("Content-Type")
        ).findAny();

        Map.Entry<String, String> entity = header.orElse(null);
        assert entity != null;
        // application/json multipart/form-data
        String contentType = entity.getValue();
        int idx = contentType.indexOf(";");
        if (idx > 0) {
            return contentType.substring(0, idx);
        } else {
            return contentType;
        }
    }


}

package io.github.NEVERMAIN.gateway.core.socket.agreement;

import io.github.NEVERMAIN.gateway.core.circuitbreaker.BreakerContext;
import io.github.NEVERMAIN.gateway.core.mapping.HttpStatement;
import io.netty.util.AttributeKey;

/**
 * @description 协议参数
 */
public class AgreementConstants {

    public static final AttributeKey<HttpStatement> HTTP_STATEMENT = AttributeKey.valueOf("HttpStatement");

    public static final AttributeKey<BreakerContext> BREAKER_CONTEXT = AttributeKey.valueOf("BreakerContext");

    public static final AttributeKey<String> TRACE_ID_KEY = AttributeKey.valueOf("gateway.traceId");

    public enum ResponseCode{

        _200("200","访问成功"),
        _400("400","接收数据的数据类型不匹配"),
        _403("403","服务器拒绝请求"),
        _404("404","服务器找不到请求的网页,输入链接有误"),
        _500("500","服务器发生错误,无法完成请求"),
        _502("502","服务器作为网关或代理，从上游服务器收到无效响应"),
        _503("502"," Service Unavailable"),

        ;

        private String code;

        private String  info;

        ResponseCode(String code, String info) {
            this.code = code;
            this.info = info;
        }

        public String getCode() {
            return code;
        }

        public String getInfo() {
            return info;
        }
    }


}

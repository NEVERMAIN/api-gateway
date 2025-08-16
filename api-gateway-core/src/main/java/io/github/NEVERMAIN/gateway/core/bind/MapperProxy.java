package io.github.NEVERMAIN.gateway.core.bind;

import io.github.NEVERMAIN.gateway.core.session.GatewaySession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @description 映射代理调用
 */
public class MapperProxy implements InvocationHandler {

    /**
     * 网关会话
     */
    private final GatewaySession gatewaySession;

    /**
     * 网关服务 URI
     */
    private final String  uri;

    public MapperProxy(String uri, GatewaySession gatewaySession) {
        this.gatewaySession = gatewaySession;
        this.uri = uri;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MapperMethod mapperMethod = new MapperMethod(uri,method,gatewaySession.getConfiguration());
        // 暂时只获取第 0 个参数
        Map<String,Object> params = (Map<String, Object>) args[0];
        return mapperMethod.execute(gatewaySession, params);
    }
}

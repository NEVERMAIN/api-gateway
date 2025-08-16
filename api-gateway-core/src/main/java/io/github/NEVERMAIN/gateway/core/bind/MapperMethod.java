package io.github.NEVERMAIN.gateway.core.bind;

import io.github.NEVERMAIN.gateway.core.mapping.HttpCommandType;
import io.github.NEVERMAIN.gateway.core.session.Configuration;
import io.github.NEVERMAIN.gateway.core.session.GatewaySession;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @description 映射方法
 */
public class MapperMethod {

    /**
     * 网关接口: /wg/activity/sayHi
     */
    String methodName;
    /**
     * HTTP 指令
     */
    private final HttpCommandType commandType;

    public MapperMethod(String uri, Method method, Configuration configuration) {
        this.methodName = configuration.getHttpStatement(uri).getMethodName();
        this.commandType = configuration.getHttpStatement(uri).getCommandType();
    }

    public Object execute(GatewaySession gatewaySession, Map<String, Object> params) {
        Object result = null;
        switch (commandType) {
            case GET:
                result = gatewaySession.getAsync(methodName, params);
                break;
            case POST:
                result = gatewaySession.getAsync(methodName, params);
                break;
            case PUT:
                break;
            case DELETE:
                break;
            default:
                throw new RuntimeException("Unknown commandType " + commandType);
        }
        return result;
    }

}

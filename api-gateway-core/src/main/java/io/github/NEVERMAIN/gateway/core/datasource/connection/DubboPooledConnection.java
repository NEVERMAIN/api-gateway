package io.github.NEVERMAIN.gateway.core.datasource.connection;

import io.github.NEVERMAIN.gateway.core.datasource.Connection;
import io.github.NEVERMAIN.gateway.core.mapping.HttpStatement;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.service.GenericService;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: Dubbo池化连接对象
 */
public class DubboPooledConnection implements Connection {

    private final GenericService genericService;
    private final HttpStatement httpStatement;
    private final ConcurrentHashMap<String, GenericService> serviceCache;

    public DubboPooledConnection(GenericService genericService, HttpStatement httpStatement, ConcurrentHashMap<String, GenericService> serviceCache) {
        this.genericService = genericService;
        this.httpStatement = httpStatement;
        this.serviceCache = serviceCache;
    }

    @Override
    public Object execute(String method, String[] parameterTypes, String[] parametersName, Object[] args) {
        try {
            return genericService.$invoke(method, parameterTypes, args);
        } catch (RpcException e) {
            // 如果调用失败,可能是 provider 挂了,或者调用超时,重新获取一个
            String key = httpStatement.getApplication() + ":" + httpStatement.getInterfaceName();
            serviceCache.remove(key);
            throw e;
        }

    }
}

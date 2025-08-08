package io.github.NEVERMAIN.gateway.core.bind;

import io.github.NEVERMAIN.gateway.core.mapping.HttpStatement;
import io.github.NEVERMAIN.gateway.core.session.Configuration;
import io.github.NEVERMAIN.gateway.core.session.GatewaySession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 泛化调用注册类
 */
public class MapperRegistry {

    private final Configuration configuration;

    // 存储泛化调用工厂的映射表
    private final Map<String, MapperProxyFactory> knownMappers = new ConcurrentHashMap<>();

    public MapperRegistry(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * 获取泛化调用服务接口方法
     *
     * @param uri 网关接口 URI
     * @return 泛化调用服务接口
     */
    public IGenericReference getMapper(String uri, GatewaySession gatewaySession) {
        final MapperProxyFactory mapperProxyFactory = knownMappers.get(uri);
        if (mapperProxyFactory == null) {
            throw new RuntimeException("Uri [" + uri + "] is not known to the MapperRegistry.");
        }
        try{
            return mapperProxyFactory.newInstance(gatewaySession);
        }catch (Exception e){
            throw new RuntimeException("Error getting mapper instance. Cause:",e);
        }

    }


    /**
     * 注册泛化调用服务接口方法
     *
     * @param httpStatement HTTP 映射消息
     */
    public void addMapper(HttpStatement httpStatement) {

        String uri = httpStatement.getUri();
        // 如果重复注册则报错
        if(hasMapper( uri)){
            throw new RuntimeException("Uri [" + uri + "] is already known to the MapperRegistry.");
        }
        knownMappers.put(uri, new MapperProxyFactory(uri));
        // 保存接口映射消息
        configuration.addHttpStatement(httpStatement);

    }

    public <T> boolean hasMapper(String uri){
        return knownMappers.containsKey(uri);
    }


}
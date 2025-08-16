package io.github.NEVERMAIN.gateway.core.bind;

import io.github.NEVERMAIN.gateway.core.session.GatewaySession;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.InvocationHandler;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 泛化调用静态代理工厂
 */
public class MapperProxyFactory {


    /**
     * 接口 uri
     */
    private final String uri;

    /**
     * 缓存代理对象
     */
    private final Map<String, IGenericReference> genericReferenceCache = new ConcurrentHashMap<>();

    public MapperProxyFactory(String uri){
        this.uri = uri;
    }

    /**
     * 创建代理对象
     * @param gatewaySession 网关会话
     * @return 代理对象
     */
    public IGenericReference newInstance(GatewaySession gatewaySession){

        return genericReferenceCache.computeIfAbsent(uri,k -> {

            try{
                // 1. 创建 InvocationHandler 实现泛化调用逻辑
                InvocationHandler handler = new MapperProxy(uri,gatewaySession);

                // 2.使用 byteBuddy 动态生成类,实现 IGenericReference 接口
                Class<?> dynamicType = new ByteBuddy()
                        .subclass(Object.class)
                        .implement(IGenericReference.class)
                        .method(ElementMatchers.named("$invokeAsync"))
                        .intercept(InvocationHandlerAdapter.of(handler))
                        .make()
                        .load(getClass().getClassLoader())
                        .getLoaded();

                // 3. 实例化代理类
                return (IGenericReference) dynamicType.getDeclaredConstructor().newInstance();
            }catch (Exception e){
                throw new RuntimeException("Failed to create proxy for method: " + uri, e);
            }

        });
    }


}

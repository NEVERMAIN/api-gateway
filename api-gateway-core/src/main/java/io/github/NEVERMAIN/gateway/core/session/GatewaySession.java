package io.github.NEVERMAIN.gateway.core.session;

import io.github.NEVERMAIN.gateway.core.bind.IGenericReference;

import java.util.Map;

/**
 * @description 用户处理网关 HTTP 请求
 */
public interface GatewaySession {

    /**
     * 根据 uri 进行RPC泛化调用处理
     * @param methodName 方法名
     * @param params 参数
     * @return
     */
    Object get(String  methodName, Map<String, Object> params);

    /**
     * 根据 uri 进行 RPC 泛化调用处理
     * @param methodName
     * @param params
     * @return
     */
    Object post(String methodName, Map<String, Object> params);

    /**
     * 根据 uri 获取泛化调用服务
     * @return  RPC 泛化调用服务类
     */
    IGenericReference getMapper();

    /**
     * 获取接口配置项
     * @return
     */
    Configuration getConfiguration();

}

package io.github.NEVERMAIN.gateway.center.api;


import io.github.NEVERMAIN.gateway.center.api.response.Response;

/**
 * @description: RPC 服务注册管理
 */
public interface IRpcRegisterManage {

    Response<Boolean> registerApplication(String systemId, String systemName, String systemRegistry, String systemAddress);


    Response<Boolean> registerApplicationInterface(String systemId, String interfaceId, String interfaceName, String protocolType, String interfaceVersion);

    Response<Boolean> registerApplicationInterfaceMethod(String systemId, String interfaceId, String methodId,
                                                         String methodName, String parameterTypes, String uri,
                                                         String httpCommandType, Integer auth);

    Response<Boolean> registerEvent(String systemId);


}

package io.github.NEVERMAIN.gateway.sdk.domain.service;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import io.github.NEVERMAIN.gateway.sdk.GatewayException;
import io.github.NEVERMAIN.gateway.sdk.common.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * 网关中心服务
 */
public class GatewayCenterService {


    private static final Logger log = LoggerFactory.getLogger(GatewayCenterService.class);

    private static final String REGISTER_APPLICATION_URI = "/wg/admin/register/registerApplication";

    private static final String REGISTER_APPLICATION_INTERFACE_URI = "/wg/admin/register/registerApplicationInterface";

    private static final String REGISTER_APPLICATION_INTERFACE_METHOD_URI = "/wg/admin/register/registerApplicationInterfaceMethod";

    private static final String REGISTER_EVENT_URI = "/wg/admin/register/registerEvent";

    /**
     * 注册系统应用信息
     *
     * @param address        网关管理中心地址
     * @param systemId       系统唯一ID
     * @param systemName     系统名称
     * @param systemRegistry 系统注册中心中心
     * @param systemAddress  系统地址
     */
    public void doRegisterApplication(String address, String systemId, String systemName, String systemRegistry, String systemAddress) {

        // 1.构建参数
        HashMap<String, Object> params = new HashMap<>();
        params.put("systemId", systemId);
        params.put("systemName", systemName);
        params.put("systemRegistry", systemRegistry);
        params.put("systemAddress", systemAddress);

        // 2.构建请求路径
        String fullAddress = address + REGISTER_APPLICATION_URI;

        // 3.发起请求
        String resultStr = "";
        try {
            resultStr = HttpUtil.post(fullAddress, params, 550);
        } catch (Exception e) {
            log.error("应用服务注册异常，链接资源不可用: fullAddress{}", fullAddress);
            throw e;
        }
        Response<Boolean> response = JSON.parseObject(resultStr, new TypeReference<Response<Boolean>>() {
        });
        log.info("向网关中心注册应用服务 systemId:{} systemName:{} 注册成果:{}", systemId, systemName, resultStr);
        if (!"0000".equals(response.getCode()) && !"0003".equals(response.getCode())) {
            throw new GatewayException("注册应用服务异常 [systemId：" + systemId + "] 、[systemRegistry：" + systemRegistry + "]");
        }
    }

    /**
     * 注册应用服务接口信息
     *
     * @param address          网关管理中心地址
     * @param systemId         系统唯一ID
     * @param interfaceId      接口唯一ID
     * @param interfaceName    接口名称
     * @param protocolType     访问协议类型
     * @param interfaceVersion 接口版本
     */
    public void doRegisterApplicationInterface(String address, String systemId, String interfaceId,
                                               String interfaceName, String protocolType, String interfaceVersion) {
        // 1.构建参数
        HashMap<String, Object> params = new HashMap<>();
        params.put("systemId", systemId);
        params.put("interfaceId", interfaceId);
        params.put("interfaceName", interfaceName);
        params.put("protocolType", protocolType);
        params.put("interfaceVersion", interfaceVersion);

        // 2.构建请求路径
        String fullAddress = address + REGISTER_APPLICATION_INTERFACE_URI;

        // 3.发起请求
        String resultStr = "";
        try {
            resultStr = HttpUtil.post(fullAddress, params, 550);
        } catch (Exception e) {
            log.error("应用服务接口注册异常，链接资源不可用: fullAddress{}", fullAddress);
            throw e;
        }
        Response<Boolean> response = JSON.parseObject(resultStr, new TypeReference<Response<Boolean>>() {
        });
        log.info("向网关中心注册应用服务 systemId:{} interfaceId:{} interfaceName:{} 注册成果:{}", systemId, interfaceId, interfaceName, resultStr);
        if (!"0000".equals(response.getCode()) && !"0003".equals(response.getCode())) {
            throw new GatewayException("向网关中心注册应用接口服务异常 [systemId：" + systemId + "] 、[interfaceId：" + interfaceId + "]");
        }
    }

    /**
     * 注册应用服务接口方法信息
     *
     * @param address         网关管理中心地址
     * @param systemId        系统唯一ID
     * @param interfaceId     接口唯一ID
     * @param methodId        方法唯一ID
     * @param methodName      方法名称
     * @param parameterTypes  参数类型
     * @param parameterName   参数名称
     * @param uri             请求URI
     * @param httpCommandType HTTP 请求命令类型
     * @param auth             认证方式
     */
    public void doRegisterApplicationInterfaceMethod(String address, String systemId,
                                                     String interfaceId, String methodId,
                                                     String methodName, String parameterTypes,
                                                     String parameterName, String uri,
                                                     String httpCommandType, Integer auth) {

        // 1.构建参数
        HashMap<String, Object> params = new HashMap<>();
        params.put("systemId", systemId);
        params.put("interfaceId", interfaceId);
        params.put("methodId", methodId);
        params.put("methodName", methodName);
        params.put("parameterTypes", parameterTypes);
        params.put("parameterName", parameterName);
        params.put("uri", uri);
        params.put("httpCommandType", httpCommandType);
        params.put("auth", auth);

        // 2.构建请求路径
        String fullAddress = address + REGISTER_APPLICATION_INTERFACE_METHOD_URI;

        // 3.发起请求
        String resultStr = "";
        try {
            resultStr = HttpUtil.post(fullAddress, params, 550);
        } catch (Exception e) {
            log.error("应用服务接口注册方法异常，链接资源不可用: fullAddress{}", fullAddress);
            throw e;
        }
        Response<Boolean> response = JSON.parseObject(resultStr, new TypeReference<Response<Boolean>>() {
        });
        log.info("向网关中心注册应用接口方法服务 systemId：{} interfaceId：{} methodId：{} 注册结果：{}", systemId, interfaceId, methodId, resultStr);
        if (!"0000".equals(response.getCode()) && !"0003".equals(response.getCode())) {
            throw new GatewayException("向网关中心注册应用接口方法服务异常 [systemId：" + systemId + "] 、[interfaceId：" + interfaceId + "]、[methodId：]" + methodId + "]");
        }

    }

    public void doRegisterEvent(String address, String systemId) {

        // 1.构建参数
        HashMap<String, Object> params = new HashMap<>();
        params.put("systemId", systemId);

        // 2.构建请求路径
        String fullAddress = address + REGISTER_EVENT_URI;

        // 3.发送请求
        String resultStr = "";
        try {
            resultStr = HttpUtil.post(fullAddress, params, 550);
        } catch (Exception e) {
            log.error("应用服务接口事件方法异常，链接资源不可用：fullAddress{}", fullAddress);
            throw e;
        }

        // 4.解析返回结果
        Response<Boolean> response = JSON.parseObject(resultStr, new TypeReference<Response<Boolean>>() {
        });
        log.info("应用服务接口事件方法 systemId：{} 注册结果：{}", systemId, resultStr);
        if (!"0000".equals(response.getCode())) {
            throw new GatewayException("向网关中心注册应用接口服务异常 [systemId：" + systemId + "] ");
        }

    }


}

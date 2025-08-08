package io.github.NEVERMAIN.gateway.sdk.application;

import com.alibaba.fastjson2.JSON;
import io.github.NEVERMAIN.gateway.sdk.GatewayException;
import io.github.NEVERMAIN.gateway.sdk.annotation.ApiProducerClazz;
import io.github.NEVERMAIN.gateway.sdk.annotation.ApiProducerMethod;
import io.github.NEVERMAIN.gateway.sdk.config.GatewaySDKServiceProperties;
import io.github.NEVERMAIN.gateway.sdk.domain.service.GatewayCenterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Method;

/**
 * @description 服务接口注册的后置处理器
 */
public class GatewaySDKApplication implements BeanPostProcessor {

    private static final Logger log = LoggerFactory.getLogger(GatewaySDKApplication.class);
    private GatewaySDKServiceProperties properties;
    private GatewayCenterService gatewayCenterService;

    public GatewaySDKApplication(GatewaySDKServiceProperties properties, GatewayCenterService gatewayCenterService) {
        this.properties = properties;
        this.gatewayCenterService = gatewayCenterService;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        ApiProducerClazz apiProducerClazz = bean.getClass().getAnnotation(ApiProducerClazz.class);
        if (null == apiProducerClazz) return bean;

        // 1.系统信息
        log.info("\n系统应用注册:系统信息 \nsystemId: {} \nsystemName: {} \nsystemType: {} \nsystemRegistry: {}", properties.getSystemId(), properties.getSystemName(), "RPC", properties.getSystemRegistry());
        gatewayCenterService.doRegisterApplication(properties.getAddress(), properties.getSystemId(),
                properties.getSystemName(), "RPC", properties.getSystemRegistry());

        // 2.接口信息
        Class<?>[] interfaces = bean.getClass().getInterfaces();
        if (interfaces.length != 1) {
            throw new GatewayException(bean.getClass().getName() + "interfaces not one this is " + JSON.toJSONString(interfaces));
        }
        String interfaceId = interfaces[0].getName();
        log.info("\n系统应用注册:接口信息 \nsystemId: {} \ninterfaceId: {} \ninterfaceName: {} \ninterfaceVersion: {}", properties.getSystemId(), interfaceId, apiProducerClazz.interfaceName(), apiProducerClazz.interfaceVersion());
        gatewayCenterService.doRegisterApplicationInterface(properties.getAddress(), properties.getSystemId(),
                interfaceId, apiProducerClazz.interfaceName(), apiProducerClazz.interfaceVersion());

        // 3.方法信息
        Method[] methods = bean.getClass().getDeclaredMethods();
        for (Method method : methods) {
            ApiProducerMethod apiProducerMethod = method.getAnnotation(ApiProducerMethod.class);
            if (apiProducerMethod == null) continue;
            // 解析参数
            Class<?>[] parameterTypes = method.getParameterTypes();
            StringBuilder parameters = new StringBuilder();
            for (Class<?> clazz : parameterTypes) {
                parameters.append(clazz.getName()).append(",");
            }
            String parameterType = parameters.toString().substring(0, parameters.toString().lastIndexOf(","));
            log.info("\n系统应用注册: 方法信息 \nsystemId: {} \ninterfaceId: {} \nmethodId: {} \nmethodName: {} \nparameterType: {} \nuri: {} \nhttpCommandType: {} \nauth: {}",
                    properties.getSystemId(),
                    bean.getClass().getName(),
                    method.getName(),
                    apiProducerMethod.methodName(),
                    parameterType,
                    apiProducerMethod.uri(),
                    apiProducerMethod.httpCommandType(),
                    apiProducerMethod.auth());

            gatewayCenterService.doRegisterApplicationInterfaceMethod(properties.getAddress(), properties.getSystemId(),
                    interfaceId, method.getName(), apiProducerMethod.methodName(), parameterType, apiProducerMethod.uri(),
                    apiProducerMethod.httpCommandType(), apiProducerMethod.auth());

        }

        // 注册完成，执行事件通知
        gatewayCenterService.doRegisterEvent(properties.getAddress(), properties.getSystemId());

        return bean;
    }
}

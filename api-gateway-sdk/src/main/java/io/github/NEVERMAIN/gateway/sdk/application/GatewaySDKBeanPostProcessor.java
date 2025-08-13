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
import java.lang.reflect.Parameter;

/**
 * @description 服务接口注册的后置处理器
 */
public class GatewaySDKBeanPostProcessor implements BeanPostProcessor {

    private static final Logger logger = LoggerFactory.getLogger(GatewaySDKBeanPostProcessor.class);
    private GatewaySDKServiceProperties properties;
    private GatewayCenterService gatewayCenterService;

    public GatewaySDKBeanPostProcessor(GatewaySDKServiceProperties properties, GatewayCenterService gatewayCenterService) {
        this.properties = properties;
        this.gatewayCenterService = gatewayCenterService;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        ApiProducerClazz apiProducerClazz = bean.getClass().getAnnotation(ApiProducerClazz.class);
        if (null == apiProducerClazz) return bean;

        // 2.接口信息
        String protocolType = apiProducerClazz.protocolType();
        Class<?>[] interfaces = bean.getClass().getInterfaces();
        String interfaceId = "";
        if (protocolType.equalsIgnoreCase("RPC")) {
            if (interfaces.length != 1) {
                throw new GatewayException(bean.getClass().getName() + "interfaces not one this is " + JSON.toJSONString(interfaces));
            }
            interfaceId = interfaces[0].getName();
        } else if (protocolType.equalsIgnoreCase("HTTP")) {
            interfaceId = bean.getClass().getName();
        }

        logger.info("\n系统应用注册:接口信息 \nsystemId: {} \ninterfaceId: {} \ninterfaceName: {} \nprotocol_type:{} \ninterfaceVersion: {}",
                properties.getSystemId(), interfaceId, apiProducerClazz.interfaceName(), protocolType, apiProducerClazz.interfaceVersion());
        gatewayCenterService.doRegisterApplicationInterface(properties.getAddress(), properties.getSystemId(),
                interfaceId, apiProducerClazz.interfaceName(), protocolType, apiProducerClazz.interfaceVersion());

        // 3.方法信息
        Method[] methods = bean.getClass().getDeclaredMethods();
        for (Method method : methods) {
            ApiProducerMethod apiProducerMethod = method.getAnnotation(ApiProducerMethod.class);
            if (apiProducerMethod == null) continue;
            // 解析参数类型
            Class<?>[] parameterTypes = method.getParameterTypes();
            StringBuilder parametersTypeBuilder = new StringBuilder();
            for (Class<?> clazz : parameterTypes) {
                parametersTypeBuilder.append(clazz.getName()).append(",");
            }
            String parameterType = parametersTypeBuilder.toString().substring(0, parametersTypeBuilder.toString().lastIndexOf(","));

            Parameter[] parametersName = method.getParameters();
            StringBuilder parametersNameBuilder = new StringBuilder();
            for (Parameter parameter : parametersName) {
                parametersNameBuilder.append(parameter.getName()).append(",");
            }
            String parameterName = parametersNameBuilder.toString().substring(0, parametersNameBuilder.toString().lastIndexOf(","));

            logger.info("\n系统应用注册: 方法信息 \nsystemId: {} \ninterfaceId: {} \nmethodId: {} \nmethodName: {} \nparameterType: {} \nparameterName:{} \nuri: {} \nhttpCommandType: {} \nauth: {}",
                    properties.getSystemId(), bean.getClass().getName(), method.getName(), apiProducerMethod.methodName(),
                    parameterType, parameterName, apiProducerMethod.uri(), apiProducerMethod.httpCommandType(), apiProducerMethod.auth());

            gatewayCenterService.doRegisterApplicationInterfaceMethod(properties.getAddress(), properties.getSystemId(),
                    interfaceId, method.getName(), apiProducerMethod.methodName(), parameterType, parameterName,
                    apiProducerMethod.uri(), apiProducerMethod.httpCommandType(), apiProducerMethod.auth());

        }

        // 注册完成，执行事件通知
        gatewayCenterService.doRegisterEvent(properties.getAddress(), properties.getSystemId());

        return bean;
    }
}

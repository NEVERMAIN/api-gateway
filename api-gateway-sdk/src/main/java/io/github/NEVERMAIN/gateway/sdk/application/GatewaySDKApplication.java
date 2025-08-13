package io.github.NEVERMAIN.gateway.sdk.application;

import io.github.NEVERMAIN.gateway.sdk.config.GatewaySDKServiceProperties;
import io.github.NEVERMAIN.gateway.sdk.domain.service.GatewayCenterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class GatewaySDKApplication implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(GatewaySDKApplication.class);

    private GatewaySDKServiceProperties properties;
    private GatewayCenterService gatewayCenterService;


    public GatewaySDKApplication(GatewaySDKServiceProperties properties,
                                 GatewayCenterService gatewayCenterService){
        this.properties = properties;
        this.gatewayCenterService = gatewayCenterService;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 1.系统应用信息
        logger.info("\n系统应用注册:系统信息 \nsystemId: {} \nsystemName: {} \nsystemRegistry: {} \nsystemAddress:{} ", properties.getSystemId(),
                properties.getSystemName(), properties.getSystemRegistry(), properties.getSystemAddress());
        gatewayCenterService.doRegisterApplication(properties.getAddress(), properties.getSystemId(),
                properties.getSystemName(), properties.getSystemRegistry(), properties.getSystemAddress());
    }
}

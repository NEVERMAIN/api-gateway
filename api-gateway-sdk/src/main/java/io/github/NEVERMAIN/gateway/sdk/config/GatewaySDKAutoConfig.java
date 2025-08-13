package io.github.NEVERMAIN.gateway.sdk.config;

import io.github.NEVERMAIN.gateway.sdk.application.GatewaySDKApplication;
import io.github.NEVERMAIN.gateway.sdk.application.GatewaySDKBeanPostProcessor;
import io.github.NEVERMAIN.gateway.sdk.domain.service.GatewayCenterService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(GatewaySDKServiceProperties.class)
public class GatewaySDKAutoConfig {

    @Bean
    public GatewaySDKBeanPostProcessor gatewaySDKBeanPostProcessor(GatewaySDKServiceProperties properties,
                                                                   GatewayCenterService gatewayCenterService) {
        return new GatewaySDKBeanPostProcessor(properties, gatewayCenterService);
    }

    @Bean
    public GatewayCenterService gatewayCenterService() {
        return new GatewayCenterService();
    }

    @Bean
    public GatewaySDKApplication gatewaySDKApplication(GatewaySDKServiceProperties properties,
                                                       GatewayCenterService gatewayCenterService) {
        return new GatewaySDKApplication(properties, gatewayCenterService);
    }


}

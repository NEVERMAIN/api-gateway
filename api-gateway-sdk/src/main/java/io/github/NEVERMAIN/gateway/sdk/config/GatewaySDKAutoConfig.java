package io.github.NEVERMAIN.gateway.sdk.config;

import io.github.NEVERMAIN.gateway.sdk.application.GatewaySDKApplication;
import io.github.NEVERMAIN.gateway.sdk.domain.service.GatewayCenterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(GatewaySDKServiceProperties.class)
public class GatewaySDKAutoConfig {


    private Logger logger = LoggerFactory.getLogger(GatewaySDKAutoConfig.class);

    @Bean
    public GatewaySDKApplication gatewaySDKApplication(GatewaySDKServiceProperties properties,
                                                       GatewayCenterService gatewayCenterService){
        return new GatewaySDKApplication(properties,gatewayCenterService);
    }

    @Bean
    public GatewayCenterService gatewayCenterService(){
       return new GatewayCenterService();
    }


}

package io.github.NEVERMAIN.gateway.assist.application;

import com.alibaba.fastjson2.JSON;
import io.github.NEVERMAIN.gateway.assist.config.GatewayServiceProperties;
import io.github.NEVERMAIN.gateway.assist.domain.model.aggregates.ApplicationSystemRichInfo;
import io.github.NEVERMAIN.gateway.assist.domain.model.vo.ApplicationInterfaceDTO;
import io.github.NEVERMAIN.gateway.assist.domain.model.vo.ApplicationInterfaceMethodDTO;
import io.github.NEVERMAIN.gateway.assist.domain.model.vo.ApplicationSystemDTO;
import io.github.NEVERMAIN.gateway.assist.domain.service.GatewayCenterService;
import io.github.NEVERMAIN.gateway.core.mapping.HttpCommandType;
import io.github.NEVERMAIN.gateway.core.mapping.HttpStatement;
import io.github.NEVERMAIN.gateway.core.session.Configuration;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import java.util.List;

/**
 * @description: 网关应用:与Spring链接,调用网关注册和接口拉取
 */
public class GatewayApplication implements ApplicationContextAware, ApplicationListener<ContextClosedEvent> {

    private static final Logger log = LoggerFactory.getLogger(GatewayApplication.class);

    private GatewayServiceProperties properties;

    private GatewayCenterService gatewayCenterService;

    private Configuration configuration;

    private Channel gatewaySocketServerChannel;

    public GatewayApplication(GatewayServiceProperties gatewayServiceProperties,
                              GatewayCenterService gatewayCenterService,
                              Configuration configuration,
                              Channel gatewaySocketServerChannel) {
        this.properties = gatewayServiceProperties;
        this.gatewayCenterService = gatewayCenterService;
        this.configuration = configuration;
        this.gatewaySocketServerChannel = gatewaySocketServerChannel;
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        try {
            if (gatewaySocketServerChannel.isActive()) {
                log.info("应用容器关闭,API网关服务关闭 localAddress:{}", gatewaySocketServerChannel.localAddress());
                gatewaySocketServerChannel.close().sync();
            }
        } catch (Exception e) {
            log.error("应用容器关闭,API网关服务关闭失败 ", e);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        try {
            // 1.注册网关服务.每一个用于转换 HTTP 协议泛化调用到 RPC 接口的网关都是一个算力，这些算力需要注册网关配置中心
            gatewayCenterService.doRegister(properties.getAddress(),
                    properties.getGroupId(),
                    properties.getGatewayId(),
                    properties.getGatewayName(),
                    properties.getGatewayAddress());

            // 2.拉取网关服务对应的系统接口信息
            addMappers();

        } catch (Exception e) {
            log.error("网关服务启动失败，停止服务。{}", e.getMessage(), e);
            throw e;
        }
    }

    public void receiveMessage(Object message){
        log.info("【事件通知】接收注册中心推送消息 message：{}", message);
        addMappers(message.toString().substring(1, message.toString().length() - 1));
    }


    public void addMappers(){
        addMappers("");
    }

    public void addMappers(String systemId) {
        // 2.拉取网关服务对应的系统接口信息
        ApplicationSystemRichInfo applicationSystemRichInfo = gatewayCenterService.pullApplicationSystemRichInfo(properties.getAddress(), properties.getGatewayId(), systemId);
        log.info("网关服务拉取接口,接口信息:{}", JSON.toJSONString(applicationSystemRichInfo));
        // 3.将待注册的接口的信息添加到配置类中
        List<ApplicationSystemDTO> applicationSystemDTOList = applicationSystemRichInfo.getApplicationSystemDTOList();
        for (ApplicationSystemDTO systemInfo : applicationSystemDTOList) {
            List<ApplicationInterfaceDTO> interfaceInfoList = systemInfo.getApplicationInterfaceDTOList();
            for (ApplicationInterfaceDTO interfaceInfo : interfaceInfoList) {
                // 3.1.创建配置信息,加载注册到配置类
                configuration.registryConfig(systemInfo.getSystemId(), systemInfo.getSystemRegistry(), interfaceInfo.getInterfaceId(), interfaceInfo.getInterfaceVersion());
                // 3.2. 注册系统服务接口信息
                List<ApplicationInterfaceMethodDTO> methodInfoList = interfaceInfo.getApplicationInterfaceMethodDTOList();
                for (ApplicationInterfaceMethodDTO methodInfo : methodInfoList) {

                    HttpStatement httpStatement = new HttpStatement(
                            systemInfo.getSystemId(),
                            interfaceInfo.getInterfaceId(),
                            methodInfo.getMethodId(),
                            methodInfo.getUri(),
                            HttpCommandType.valueOf(methodInfo.getHttpCommandType()),
                            methodInfo.getParameterType(),
                            methodInfo.getAuth().equals(1));

                    configuration.addMapper(httpStatement);
                    log.info("网关服务注册映射方法 系统:{} 接口:{} 方法:{}", systemInfo.getSystemId(), interfaceInfo.getInterfaceId(), methodInfo.getMethodId());

                }
            }
        }
    }


}

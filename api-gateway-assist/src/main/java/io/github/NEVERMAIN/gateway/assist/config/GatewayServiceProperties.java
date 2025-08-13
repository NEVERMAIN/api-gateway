package io.github.NEVERMAIN.gateway.assist.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 网关服务配置
 */
@ConfigurationProperties(prefix = "api-gateway")
public class GatewayServiceProperties {

    /** 网关管理中心地址 */
    private String address;
    /** 服务分组ID */
    private String groupId;
    /** 网关唯一标识ID */
    private String gatewayId;
    /** 网关名称 */
    private String gatewayName;
    /** 网关地址 */
    private String gatewayAddress;

    public GatewayServiceProperties() {
    }

    public GatewayServiceProperties(String address, String gatewayAddress, String gatewayName, String gatewayId, String groupId) {
        this.address = address;
        this.gatewayAddress = gatewayAddress;
        this.gatewayName = gatewayName;
        this.gatewayId = gatewayId;
        this.groupId = groupId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(String gatewayId) {
        this.gatewayId = gatewayId;
    }

    public String getGatewayName() {
        return gatewayName;
    }

    public void setGatewayName(String gatewayName) {
        this.gatewayName = gatewayName;
    }

    public String getGatewayAddress() {
        return gatewayAddress;
    }

    public void setGatewayAddress(String gatewayAddress) {
        this.gatewayAddress = gatewayAddress;
    }
}

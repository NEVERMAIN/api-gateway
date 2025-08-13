package io.github.NEVERMAIN.gateway.assist.domain.model.vo;

import java.util.List;

public class ApplicationSystemDTO {

    /**
     * 系统ID
     */
    private String systemId;
    /**
     * 应用名称
     */
    private String systemName;
    /**
     * 系统类型：RPC、HTTP
     */
    private String systemType;
    /**
     * 系统注册中心：zookeeper://192.168.198.138:2181
     */
    private String systemRegistry;
    /**
     * 系统地址 http://127.0.0.1:8080
     */
    private String systemAddress;

    /**
     * 应用接口信息列表
     */
    private List<ApplicationInterfaceDTO> applicationInterfaceDTOList;

    public ApplicationSystemDTO() {
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getSystemRegistry() {
        return systemRegistry;
    }

    public void setSystemRegistry(String systemRegistry) {
        this.systemRegistry = systemRegistry;
    }

    public String getSystemType() {
        return systemType;
    }

    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public List<ApplicationInterfaceDTO> getApplicationInterfaceDTOList() {
        return applicationInterfaceDTOList;
    }

    public void setApplicationInterfaceDTOList(List<ApplicationInterfaceDTO> applicationInterfaceDTOList) {
        this.applicationInterfaceDTOList = applicationInterfaceDTOList;
    }

    public String getSystemAddress() {
        return systemAddress;
    }

    public void setSystemAddress(String systemAddress) {
        this.systemAddress = systemAddress;
    }
}

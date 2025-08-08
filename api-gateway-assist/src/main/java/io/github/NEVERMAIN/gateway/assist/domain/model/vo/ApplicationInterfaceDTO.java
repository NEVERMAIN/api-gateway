package io.github.NEVERMAIN.gateway.assist.domain.model.vo;

import java.util.List;

public class ApplicationInterfaceDTO {

    /**
     * 应用唯一标识
     */
    private String systemId;
    /**
     * 系统接口唯一标识
     */
    private String interfaceId;
    /**
     * 服务接口名称
     */
    private String interfaceName;
    /**
     * 接口版本
     */
    private String interfaceVersion;
    /**
     * 接口方法信息
     */
    private List<ApplicationInterfaceMethodDTO> applicationInterfaceMethodDTOList;

    public ApplicationInterfaceDTO() {
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public List<ApplicationInterfaceMethodDTO> getApplicationInterfaceMethodDTOList() {
        return applicationInterfaceMethodDTOList;
    }

    public void setApplicationInterfaceMethodDTOList(List<ApplicationInterfaceMethodDTO> applicationInterfaceMethodDTOList) {
        this.applicationInterfaceMethodDTOList = applicationInterfaceMethodDTOList;
    }

    public String getInterfaceVersion() {
        return interfaceVersion;
    }

    public void setInterfaceVersion(String interfaceVersion) {
        this.interfaceVersion = interfaceVersion;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getInterfaceId() {
        return interfaceId;
    }

    public void setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId;
    }
}

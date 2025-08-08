package io.github.NEVERMAIN.gateway.assist.domain.model.aggregates;

import io.github.NEVERMAIN.gateway.assist.domain.model.vo.ApplicationSystemDTO;

import java.util.List;

public class ApplicationSystemRichInfo {
    /**
     * 网关ID
     */
    private String gatewayId;
    /**
     * 系统列表
     */
    private List<ApplicationSystemDTO> applicationSystemDTOList;

    public ApplicationSystemRichInfo() {
    }

    public String getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(String gatewayId) {
        this.gatewayId = gatewayId;
    }

    public List<ApplicationSystemDTO> getApplicationSystemDTOList() {
        return applicationSystemDTOList;
    }

    public void setApplicationSystemDTOList(List<ApplicationSystemDTO> applicationSystemDTOList) {
        this.applicationSystemDTOList = applicationSystemDTOList;
    }
}

package io.github.NEVERMAIN.gateway.center.domain.manage.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description 应用系统信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationSystemVO {

    /**
     * 系统ID
     */
    private String systemId;
    /**
     * 应用名称
     */
    private String systemName;
    /**
     * 系统注册中心：ZK、ETCD、NACOS
     */
    private String systemRegistry;
    /**
     * 系统地址
     */
    private String systemAddress;
    /**
     * 应用接口信息列表
     */
    private List<ApplicationInterfaceVO> applicationInterfaceVOList;

}

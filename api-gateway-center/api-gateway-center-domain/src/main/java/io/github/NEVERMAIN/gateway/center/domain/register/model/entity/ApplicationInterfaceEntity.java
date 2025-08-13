package io.github.NEVERMAIN.gateway.center.domain.register.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationInterfaceEntity {

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
     * 协议类型
     */
    private String protocolType;
    /**
     * 接口版本
     */
    private String interfaceVersion;

}

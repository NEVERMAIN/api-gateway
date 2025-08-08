package io.github.NEVERMAIN.gateway.center.domain.register.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationSystemEntity {

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
     * 系统注册中心：ZK、ETCD、NACOS
     */
    private String systemRegistry;

}

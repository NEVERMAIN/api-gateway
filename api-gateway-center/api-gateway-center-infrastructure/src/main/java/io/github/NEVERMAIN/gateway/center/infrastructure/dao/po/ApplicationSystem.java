package io.github.NEVERMAIN.gateway.center.infrastructure.dao.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationSystem {

    /**
     * 自增ID
     */
    private Long id;
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
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

}

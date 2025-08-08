package io.github.NEVERMAIN.gateway.center.infrastructure.dao.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GatewayDistribution {

    /**
     * 自增ID
     */
    private Long id;
    /**
     * 分组ID
     */
    private String groupId;
    /**
     * 网关ID
     */
    private String gatewayId;
    /**
     * 系统唯一标识
     */
    private String systemId;
    /**
     * 系统名称
     */
    private String systemName;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

}

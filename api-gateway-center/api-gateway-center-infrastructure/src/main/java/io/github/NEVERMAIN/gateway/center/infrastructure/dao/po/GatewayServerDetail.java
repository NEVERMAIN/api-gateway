package io.github.NEVERMAIN.gateway.center.infrastructure.dao.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GatewayServerDetail {

    /** 自增ID */
    private Long id;
    /** 网关组ID */
    private String groupId;
    /** 网关ID */
    private String gatewayId;
    /** 网关名称 */
    private String gatewayName;
    /** 网关地址 */
    private String gatewayAddress;
    /** 服务状态：0-不可用、1-可使用 */
    private Integer status;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;

}

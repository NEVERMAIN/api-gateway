package io.github.NEVERMAIN.gateway.center.domain.manage.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GatewayServerDetailVO {

    /** 网关服务分组 ID */
    private String groupId;
    /** 网关ID */
    private String gatewayId;
    /** 网关名称 */
    private String gatewayName;
    /** 网关地址 */
    private String gatewayAddress;
    /** 服务状态：0-不可用、1-可使用 */
    private Integer status;

}

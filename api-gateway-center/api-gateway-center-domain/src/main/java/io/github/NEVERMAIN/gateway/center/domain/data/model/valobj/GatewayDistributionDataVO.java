package io.github.NEVERMAIN.gateway.center.domain.data.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GatewayDistributionDataVO {

    /** 自增主键 */
    private Long id;
    /** 分组标识 */
    private String groupId;
    /** 网关标识 */
    private String gatewayId;
    /** 系统标识 */
    private String systemId;
    /** 系统名称 */
    private String systemName;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;

    public GatewayDistributionDataVO(String groupId, String gatewayId){
        this.groupId = groupId;
        this.gatewayId = gatewayId;
    }




}

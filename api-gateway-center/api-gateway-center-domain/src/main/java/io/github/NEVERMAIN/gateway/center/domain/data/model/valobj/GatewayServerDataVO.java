package io.github.NEVERMAIN.gateway.center.domain.data.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GatewayServerDataVO {

    /** 自增主键 */
    private Long id;
    /** 分组标识 */
    private String groupId;
    /** 分组名称 */
    private String groupName;


}

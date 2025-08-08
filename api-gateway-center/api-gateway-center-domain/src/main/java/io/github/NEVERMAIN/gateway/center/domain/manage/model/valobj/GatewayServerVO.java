package io.github.NEVERMAIN.gateway.center.domain.manage.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GatewayServerVO {

    /** 分组ID */
    private String groupId;
    /** 分组名称 */
    private String groupName;

}

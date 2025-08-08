package io.github.NEVERMAIN.gateway.center.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GatewayServerDTO {

    /** 分组ID */
    private String groupId;
    /** 分组名称 */
    private String groupName;

}

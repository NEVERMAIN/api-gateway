package io.github.NEVERMAIN.gateway.center.infrastructure.dao.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GatewayServer {

    /** 自增ID */
    private Long id;
    /** 分组ID */
    private String groupId;
    /** 分组名称 */
    private String groupName;

}

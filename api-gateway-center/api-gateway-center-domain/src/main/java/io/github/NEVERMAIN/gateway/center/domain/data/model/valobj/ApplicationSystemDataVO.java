package io.github.NEVERMAIN.gateway.center.domain.data.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 应用服务 DTO 数据
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationSystemDataVO {

    /** 系统标识 */
    private String systemId;
    /** 系统名称 */
    private String systemName;
    /** 系统类型: RPC、HTTP*/
    private String systemType;
    /** 注册中心: zookeeper://127.0.0.1:2181*/
    private String systemRegistry;

    public ApplicationSystemDataVO(String systemId,String systemName){
        this.systemId = systemId;
        this.systemName = systemName;
    }

}

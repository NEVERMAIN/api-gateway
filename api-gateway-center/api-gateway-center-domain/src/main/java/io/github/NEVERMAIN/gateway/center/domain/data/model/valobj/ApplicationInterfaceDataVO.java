package io.github.NEVERMAIN.gateway.center.domain.data.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationInterfaceDataVO {

    /** 系统标识 */
    private String systemId;
    /** 接口标识 */
    private String interfaceId;
    /** 接口名称 */
    private String interfaceName;
    /** 接口版本 */
    private String interfaceVersion;

    public ApplicationInterfaceDataVO(String systemId, String interfaceId){
        this.systemId = systemId;
        this.interfaceId = interfaceId;
    }



}

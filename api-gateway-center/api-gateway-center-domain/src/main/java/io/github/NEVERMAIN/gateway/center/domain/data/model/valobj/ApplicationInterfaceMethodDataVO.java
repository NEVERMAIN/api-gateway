package io.github.NEVERMAIN.gateway.center.domain.data.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationInterfaceMethodDataVO {

    /** 系统标识 */
    private String systemId;
    /** 接口标识 */
    private String interfaceId;
    /** 方法标识 */
    private String methodId;
    /** 方法名称 */
    private String methodName;
    /** 参数类型(RPC 限定单参数注册)；new String[]{"java.lang.String"}、new String[]{"cn.bugstack.gateway.rpc.dto.XReq"} */
    private String parameterType;
    /** 网关接口 */
    private String uri;
    /** 接口类型；GET、POST、PUT、DELETE */
    private String httpCommandType;
    /** 是否鉴权；true = 1是、false = 0否 */
    private Integer auth;


    public ApplicationInterfaceMethodDataVO(String systemId, String interfaceId){
        this.systemId = systemId;
        this.interfaceId = interfaceId;
    }

}

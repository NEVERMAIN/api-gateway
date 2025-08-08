package io.github.NEVERMAIN.gateway.center.domain.api.model.entity;

import io.github.NEVERMAIN.gateway.center.domain.api.model.valobj.ApiAuthTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description API 接口信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiMetaData {

    /**
     * 应用名称；
     */
    private String application;
    /**
     * 服务接口；RPC、其他
     */
    private String interfaceName;
    /**
     * 服务方法；RPC#method
     */
    private String methodName;
    /**
     * 参数类型(RPC 限定单参数注册)；
     * new String[]{"java.lang.String"}、
     * new String[]{"io.github.NEVERMAIN.gateway.rpc.dto.XReq"}
     */
    private String parameterType;
    /**
     * 网关接口
     */
    private String uri;
    /**
     * 接口类型；GET、POST、PUT、DELETE
     */
    private String httpCommandType;
    /**
     * 是否鉴权；true = 1是、false = 0否
     */
    private ApiAuthTypeEnum auth;

}

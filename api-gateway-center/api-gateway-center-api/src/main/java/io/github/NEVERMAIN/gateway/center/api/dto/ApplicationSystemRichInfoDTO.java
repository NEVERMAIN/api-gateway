package io.github.NEVERMAIN.gateway.center.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationSystemRichInfoDTO {

    /**
     * 网关ID
     */
    private String gatewayId;
    /**
     * 系统列表
     */
    private List<ApplicationSystemDTO> applicationSystemDTOList;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApplicationSystemDTO {
        /**
         * 系统ID
         */
        private String systemId;
        /**
         * 应用名称
         */
        private String systemName;
        /**
         * 系统类型：RPC、HTTP
         */
        private String systemType;
        /**
         * 系统注册中心：ZK、ETCD、NACOS
         */
        private String systemRegistry;
        /**
         * 系统地址
         */
        private String systemAddress;
        /**
         * 应用接口信息列表
         */
        private List<ApplicationInterfaceDTO> applicationInterfaceDTOList;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApplicationInterfaceDTO {

        /**
         * 应用唯一标识
         */
        private String systemId;
        /**
         * 系统接口唯一标识
         */
        private String interfaceId;
        /**
         * 服务接口名称
         */
        private String interfaceName;
        /**
         * 协议类型：RPC、HTTP
         */
        private String protocolType;
        /**
         * 接口版本
         */
        private String interfaceVersion;
        /**
         * 接口方法信息
         */
        private List<ApplicationInterfaceMethodDTO> applicationInterfaceMethodDTOList;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApplicationInterfaceMethodDTO {
        /**
         * 应用唯一标识
         */
        private String systemId;
        /**
         * 系统接口唯一标识
         */
        private String interfaceId;
        /**
         * 系统接口方法唯一标识
         */
        private String methodId;
        /**
         * 服务方法名称
         */
        private String methodName;
        /**
         * 协议类型：RPC、HTTP
         */
        private String protocolType;
        /**
         * 参数类型(RPC 限定单参数注册):
         * new String[]{\"java.lang.String\"}、
         * new String[]{\"io.github.NEVERMAIN.gateway.rpc.dto.XReq\"}
         */
        private String parameterType;
        /**
         * 参数名称
         */
        private String parameterName;
        /**
         * 网关接口
         */
        private String uri;
        /**
         * 0-不需要鉴权 1-需要鉴权
         */
        private Integer auth;
        /**
         * 接口类型；GET、POST、PUT、DELETE
         */
        private String httpCommandType;

    }






}

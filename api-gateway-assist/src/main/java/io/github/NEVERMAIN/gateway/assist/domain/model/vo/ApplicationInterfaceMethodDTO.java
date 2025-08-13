package io.github.NEVERMAIN.gateway.assist.domain.model.vo;

public class ApplicationInterfaceMethodDTO {

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

    public ApplicationInterfaceMethodDTO() {
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getInterfaceId() {
        return interfaceId;
    }

    public void setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId;
    }

    public String getMethodId() {
        return methodId;
    }

    public void setMethodId(String methodId) {
        this.methodId = methodId;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Integer getAuth() {
        return auth;
    }

    public void setAuth(Integer auth) {
        this.auth = auth;
    }

    public String getHttpCommandType() {
        return httpCommandType;
    }

    public void setHttpCommandType(String httpCommandType) {
        this.httpCommandType = httpCommandType;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }
}

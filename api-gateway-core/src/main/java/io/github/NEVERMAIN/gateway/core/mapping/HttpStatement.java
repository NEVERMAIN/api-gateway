package io.github.NEVERMAIN.gateway.core.mapping;

/**
 * @description 网关接口映射信息
 */
public class HttpStatement {

    /**
     * 应用名称
     * eg: api-gateway-test
     */
    private String application;
    /**
     * 服务接口 RPC、HTTP
     * eg: io.github.NEVERMAIN.gateway.rpc.IActivityBooth
     */
    private String interfaceName;
    /**
     * 服务方法名: RPC#method
     * eg: sayHi
     */
    private String methodName;
    /**
     * 参数类型(RPC 限定单参数注册)
     * eg: "java.lang.String"、"io.github.NEVERMAIN.gateway.rpc.dto.XReq"
     */
    private String parameterType;
    /**
     * 网关接口
     * eg: /wg/activity/sayHi
     */
    private String uri;
    /**
     * 接口类型 GET、POST、PUT、DELETE
     */
    private HttpCommandType commandType;
    /**
     * 是否需要鉴权 true -> 需要; false -> 不需要
     */
    private  boolean auth;


    public HttpStatement(String application, String interfaceName, String methodName, String uri,
                         HttpCommandType commandType, String parameterType, boolean auth) {
        this.application = application;
        this.interfaceName = interfaceName;
        this.methodName = methodName;
        this.uri = uri;
        this.commandType = commandType;
        this.parameterType = parameterType;
        this.auth = auth;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public HttpCommandType getCommandType() {
        return commandType;
    }

    public void setCommandType(HttpCommandType commandType) {
        this.commandType = commandType;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }

    public boolean isAuth() {
        return auth;
    }

    public void setAuth(boolean auth) {
        this.auth = auth;
    }
}

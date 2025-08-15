package io.github.NEVERMAIN.gateway.core.socket.agreement;

/**
 * @description 网关结果封装
 */
public class GatewayResultMessage {

    private String code;
    private String info;
    private Object data;
    private String traceId;

    public GatewayResultMessage(String code, String info, Object data, String traceId) {
        this.code = code;
        this.info = info;
        this.data = data;
        this.traceId = traceId;
    }

    public static GatewayResultMessage buildSuccess(Object data, String traceId){
        return new GatewayResultMessage(AgreementConstants.ResponseCode._200.getCode(),
                AgreementConstants.ResponseCode._200.getInfo(),
                data,traceId);
    }

    public static GatewayResultMessage buildError(String code, String info, String traceId){
        return new GatewayResultMessage(code, info, null, traceId);
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }
}

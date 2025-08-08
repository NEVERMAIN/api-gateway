package io.github.NEVERMAIN.gateway.assist.common;

public class Response<T> {

    private String code;
    private String info;
    private T data;

    public Response(String code, String info, T data) {
        this.code = code;
        this.info = info;
        this.data = data;
    }

    public Response() {
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

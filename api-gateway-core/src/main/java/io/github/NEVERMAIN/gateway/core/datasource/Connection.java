package io.github.NEVERMAIN.gateway.core.datasource;

import java.util.concurrent.CompletionStage;

/**
 * 连接接口
 */
public interface Connection {

    /**
     * 执行
     * @param method 方法名
     * @param parameterTypes 参数类型
     * @param parametersName 参数名
     * @param args 参数值
     * @return
     */
    Object execute(String method,String[] parameterTypes , String[] parametersName, Object[] args);

    /**
     * 异步执行
     *
     * @param method         方法名
     * @param parameterTypes 参数类型
     * @param parametersName 参数名称
     * @param args           参数
     * @return
     */
    CompletionStage<Object> executeAsync(
            String method, String[] parameterTypes, String[] parametersName, Object[] args);

}

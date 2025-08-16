package io.github.NEVERMAIN.gateway.core.datasource;

import java.util.concurrent.CompletionStage;

/**
 * 数据源连接基础类
 */
public abstract class BaseConnection implements Connection {

    @Override
    public Object execute(String method, String[] parameterTypes, String[] parametersName, Object[] args) {
        return null;
    }

    @Override
    public CompletionStage<Object> executeAsync(String method, String[] parameterTypes, String[] parametersName, Object[] args) {
        return null;
    }
}

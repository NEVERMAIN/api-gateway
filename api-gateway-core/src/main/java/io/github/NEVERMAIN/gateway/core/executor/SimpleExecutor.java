package io.github.NEVERMAIN.gateway.core.executor;

import io.github.NEVERMAIN.gateway.core.datasource.Connection;
import io.github.NEVERMAIN.gateway.core.session.Configuration;

import java.util.concurrent.CompletionStage;

/**
 * @description 简单执行器
 */
public class SimpleExecutor extends BaseExecutor {

    public SimpleExecutor(Configuration configuration, Connection connection) {
        super(configuration, connection);
    }

    @Override
    protected Object doExecute(String method, String[] parameterTypes, String[] parametersName, Object[] args) {
        /*
         * 调用服务
         * 封装参数:
         *   01(允许): java.lang.String
         *   02(允许): io.github.NEVERMAIN.gateway.rpc.dto.XReq
         *   03(拒绝): java.lang.String、io.github.NEVERMAIN.gateway.rpc.dto.XReq
         */
        return connection.execute(method, parameterTypes, parametersName, args);
    }

    @Override
    protected CompletionStage<Object> doExecuteAsync(String method, String[] parameterTypes, String[] parametersName, Object[] args) {
        return connection.executeAsync(method, parameterTypes, parametersName, args);
    }
}

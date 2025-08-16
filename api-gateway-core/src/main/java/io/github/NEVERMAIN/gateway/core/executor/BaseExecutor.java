package io.github.NEVERMAIN.gateway.core.executor;

import com.alibaba.fastjson2.JSON;
import io.github.NEVERMAIN.gateway.core.datasource.Connection;
import io.github.NEVERMAIN.gateway.core.executor.result.SessionResult;
import io.github.NEVERMAIN.gateway.core.mapping.HttpStatement;
import io.github.NEVERMAIN.gateway.core.session.Configuration;
import io.github.NEVERMAIN.gateway.core.type.SimpleTypeRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.CompletionStage;

/**
 * @description 抽象执行器
 */
public abstract class BaseExecutor implements Executor {

    private static final Logger log = LoggerFactory.getLogger(BaseExecutor.class);
    /**
     * 服务配置项
     */
    protected final Configuration configuration;
    /**
     * 数据源连接
     */
    protected final Connection connection;

    public BaseExecutor(Configuration configuration, Connection connection) {
        this.configuration = configuration;
        this.connection = connection;
    }

    @Override
    public SessionResult exec(HttpStatement httpStatement, Map<String, Object> params) throws Exception {

        String methodName = httpStatement.getMethodName();
        String parameterType = httpStatement.getParameterType();
        String[] parameterTypes = new String[]{parameterType};
        Object[] args = SimpleTypeRegistry.isSimpleType(parameterType) ? params.values().toArray() :
                new Object[]{params};
        String parameterName = httpStatement.getParameterName();
        String[] parametersName = new String[]{"ignore"};
        if (parameterName != null) {
            parametersName[0] = parameterName;
        }

        log.info("执行调用 method:{}#{}.{}({}) args:{}", httpStatement.getApplication(), httpStatement.getInterfaceName(),
                httpStatement.getMethodName(), JSON.toJSONString(parameterTypes), JSON.toJSONString(args));

        try {
            Object data = doExecute(methodName, parameterTypes, parametersName, args);
            return SessionResult.buildSuccess(data);
        } catch (Exception e) {
            return SessionResult.buildError(e.getMessage());
        }

    }

    protected abstract Object doExecute(String method, String[] parameterTypes, String[] parametersName, Object[] args);

    @Override
    public CompletionStage<Object> execAsync(HttpStatement httpStatement, Map<String, Object> params) throws Exception {

        String methodName = httpStatement.getMethodName();
        String parameterType = httpStatement.getParameterType();
        String[] parameterTypes = new String[]{parameterType};
        Object[] args = SimpleTypeRegistry.isSimpleType(parameterType) ? params.values().toArray() :
                new Object[]{params};
        String parameterName = httpStatement.getParameterName();
        String[] parametersName = new String[]{"ignore"};
        if (parameterName != null) {
            parametersName[0] = parameterName;
        }

        log.info("执行调用 method:{}#{}.{}({}) args:{}", httpStatement.getApplication(), httpStatement.getInterfaceName(),
                httpStatement.getMethodName(), JSON.toJSONString(parameterTypes), JSON.toJSONString(args));
        CompletionStage<Object> result = doExecuteAsync(methodName, parameterTypes, parametersName, args);
        return result;
    }

    protected abstract CompletionStage<Object> doExecuteAsync(String method, String[] parameterTypes, String[] parametersName, Object[] args);

}

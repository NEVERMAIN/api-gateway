package io.github.NEVERMAIN.gateway.core.executor;

import io.github.NEVERMAIN.gateway.core.executor.result.SessionResult;
import io.github.NEVERMAIN.gateway.core.mapping.HttpStatement;

import java.util.Map;

/**
 * @description 执行器接口
 */
public interface Executor {

    SessionResult exec(HttpStatement httpStatement, Map<String,Object> params) throws Exception;

}

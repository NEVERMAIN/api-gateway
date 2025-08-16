package io.github.NEVERMAIN.gateway.core.bind;

import io.github.NEVERMAIN.gateway.core.executor.result.SessionResult;

import java.util.Map;
import java.util.concurrent.CompletionStage;

/**
 * @description 统一泛化调用接口
 */
public interface IGenericReference {

    /**
     * 泛化调用
     * @param params 参数
     * @return
     */
    SessionResult invoke(Map<String,Object> params);

    /**
     * 泛化调用
     * @param params 参数
     * @return
     */
    CompletionStage<Object> $invokeAsync(Map<String,Object> params);


}

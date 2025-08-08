package io.github.NEVERMAIN.gateway.core.authorization;

/**
 * @description 认证服务接口
 */
public interface IAuth {

    boolean validate(String id, String token);

}

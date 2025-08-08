package io.github.NEVERMAIN.gateway.core.session;

/**
 * @description 泛化调用会话工厂接口
 */
public interface GatewaySessionFactory {

    /**
     * 获取会话
     * @return 会话 Channel
     */
    GatewaySession openSession(String uri) ;

}

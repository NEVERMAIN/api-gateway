package io.github.NEVERMAIN.gateway.core.authorization.auth;

import io.github.NEVERMAIN.gateway.core.authorization.GatewayAuthorizingToken;
import io.github.NEVERMAIN.gateway.core.authorization.IAuth;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.ini.IniSecurityManagerFactory;
import org.apache.shiro.lang.util.Factory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;

/**
 * @description 认证服务
 */
public class AuthService implements IAuth {

    private Subject subject;

    public AuthService(){

        // 1.获取 SecurityManager 工厂
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        // 2.获取 SecurityManager 实例
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        // 3.得到 subject 及 token
        this.subject = SecurityUtils.getSubject();

    }

    @Override
    public boolean validate(String id, String token) {
        try{
            // 身份验证
            subject.login(new GatewayAuthorizingToken(id, token));
            // 返回结果
            return subject.isAuthenticated();
        }finally {
            subject.logout();
        }
    }
}

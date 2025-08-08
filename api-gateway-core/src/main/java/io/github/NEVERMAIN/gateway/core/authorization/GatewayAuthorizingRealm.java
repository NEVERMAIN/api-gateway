package io.github.NEVERMAIN.gateway.core.authorization;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Map;

/**
 * @description 认证授权
 */
public class GatewayAuthorizingRealm extends AuthorizingRealm {

    /**
     * 授权
     * @param principalCollection
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    /**
     * 认证
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        try{
            if( !(token instanceof GatewayAuthorizingToken)){
                throw new AuthenticationException("不支持的Token类型");
            }
            GatewayAuthorizingToken gatewayToken = (GatewayAuthorizingToken) token;
            String jwt = gatewayToken.getJwt();

            // 解析 JWT 并获取用户信息
            Map<String, Object> claims = JwtUtil.extractAllClaims(jwt);

            // 返回认证信息
            return new SimpleAuthenticationInfo(token.getPrincipal(), token.getCredentials(), this.getName());

        }catch (Exception e){
            throw new AuthenticationException("无效令牌",e);
        }


    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof GatewayAuthorizingToken;
    }
}

package com.paic.shiro;

import com.paic.service.UserAuthService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author PENGJIN
 * 默认用的SimpleCredentialsMatcher 密码匹配器
 */
public class SsoRealm extends AuthorizingRealm {

    @Autowired
    private UserAuthService userAuthService;

    @Override
    public  String  getName(){
        return this.getClass().getName();
    }

    public static final Logger log = LoggerFactory.getLogger(SsoRealm.class);
    /**
     * 多重写一个support
     * 标识这个Realm是专门用来验证SsoToken
     * 不负责验证其他的token（UsernamePasswordToken）
     * */
    @Override
    public boolean supports(AuthenticationToken token) {
        //这个token就是从过滤器中传入的SsoToken
        return token instanceof SsoToken;
    }

    /**授权*/
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    /**认证
    这个token就是从过滤器中传入的customerToken
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        SsoToken ssotoken = (SsoToken) token;

        String sso = new String ((char [])(ssotoken.getCredentials()));
        if (sso == null) {
            throw new NullPointerException("授权码不允许为空");
        }
        // 验证授权码
        Object o = userAuthService.authSso(sso);

        log.info("正在使用sso登录:"+sso);
        return new SimpleAuthenticationInfo(sso+"abc",sso,this.getName());

    }

}

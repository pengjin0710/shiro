package com.paic.shiro;

import com.paic.model.UserAuth;
import com.paic.service.UserAuthService;
import com.paic.util.JwtUtil;
import org.apache.shiro.authc.*;
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
public class CustomerRealm extends AuthorizingRealm {

    @Autowired
    private UserAuthService userAuthService;

    @Override
    public  String  getName(){
        return this.getClass().getName();
    }

    public static final Logger log = LoggerFactory.getLogger(CustomerRealm.class);
    /**
     * 多重写一个support
     * 标识这个Realm是专门用来验证customerToken
     * 不负责验证其他的token（UsernamePasswordToken）
     * */
    @Override
    public boolean supports(AuthenticationToken token) {
        //这个token就是从过滤器中传入的customerToken
        return token instanceof CustomerToken;
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

        CustomerToken customertoken = (CustomerToken) token;
        String username = (String) customertoken.getPrincipal();
        if (username == null) {
            throw new NullPointerException("用户名不允许为空");
        }

        String authCode = new String ((char [])(customertoken.getCredentials()));
        if (authCode == null) {
            throw new NullPointerException("密码不允许为空");
        }

        String validateCode = customertoken.getValidateCode();
        // 验证用户名、密码、验证码
        Object auth = userAuthService.auth(new UserAuth(username, authCode, validateCode));

        log.info("正在使用用户名密码登录:"+username);
        return new SimpleAuthenticationInfo(auth,authCode,this.getName());

    }

}

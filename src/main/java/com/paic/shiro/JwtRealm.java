package com.paic.shiro;

import com.auth0.jwt.exceptions.TokenExpiredException;
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
 */
public class JwtRealm extends AuthorizingRealm {

    @Autowired
    private UserAuthService userAuthService;

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    public static final Logger log = LoggerFactory.getLogger(JwtRealm.class);

    /**
     * 多重写一个support
     * 标识这个Realm是专门用来验证JwtToken
     * 不负责验证其他的token（UsernamePasswordToken）
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        //这个token就是从过滤器中传入的jwtToken
        return token instanceof JwtToken;
    }

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    /**
     * 认证
     * 这个token就是从过滤器中传入的jwtToken
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        String jwt = (String) token.getPrincipal();
        if (jwt == null) {
            throw new NullPointerException("jwtToken 不允许为空");
        }
        //判断
        JwtUtil jwtUtil = new JwtUtil();
        try {
            if (!jwtUtil.isVerify(jwt)) {
                //throw new UnknownAccountException();
            }
        } catch (TokenExpiredException e) {
            //过期则验证用户状态，并续期
        }
        //下面是验证这个user是否是真实存在的
        String username = (String) jwtUtil.decode(jwt).get("username");
        //判断数据库中username是否存在
        log.info("在使用token登录" + username);
        return new SimpleAuthenticationInfo(jwt, jwt, this.getName());
        //这里返回的是类似账号密码的东西，但是jwtToken都是jwt字符串。还需要一个该Realm的类名

    }

}

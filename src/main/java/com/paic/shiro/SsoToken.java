package com.paic.shiro;

import org.apache.shiro.authc.AuthenticationToken;

public class SsoToken implements AuthenticationToken {

    private String sso;

    public SsoToken(String sso) {
        this.sso = sso;
    }

    @Override//类似是用户名
    public Object getPrincipal() {
        return sso;
    }

    @Override//类似密码
    public Object getCredentials() {
        return sso;
    }
}
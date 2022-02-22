package com.paic.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.authz.Authorizer;
import org.apache.shiro.realm.Realm;

import java.util.List;
import java.util.stream.Collectors;

public class UserModularRealmAuthenticator extends ModularRealmAuthenticator {
    @Override
    protected AuthenticationInfo doAuthenticate(AuthenticationToken token) throws AuthenticationException {
        assertRealmsConfigured();
        //依据Realm中配置的支持Token来进行过滤
        List<Realm> realms = this.getRealms()
                .stream()
                .filter(realm -> realm.supports(token))
                .collect(Collectors.toList());
        for (Realm realm : realms) {
            if (!(realm instanceof Authorizer)) {
                continue;
            }
            String realmName = realm.getName();
            if (token instanceof CustomerToken && realmName.equals(CustomerRealm.class.getName())) {
                return doSingleRealmAuthentication(realm, token);
            }
            if (token instanceof JwtToken && realmName.equals(JwtRealm.class.getName())) {
                return doSingleRealmAuthentication(realm, token);
            }
            if (token instanceof SsoToken && realmName.equals(SsoRealm.class.getName())) {
                return doSingleRealmAuthentication(realm, token);
            }

        }
        return doMultiRealmAuthentication(realms, token);
    }
}
package com.paic.service;

import com.paic.model.UserAuth;

/**
 * @author GBD
 */
public interface UserAuthService<T> {
    /**
     *
     * @param user
     * @return 返回认证通过的用户信息，认证不通过返回空
     *         用户信息不包含敏感信息
     */
    T auth(UserAuth user);

    /**
     *
     * @param sso
     * @return 返回认证通过的用户信息，认证不通过返回空
     *         用户信息不包含敏感信息
     */
    T authSso(String sso);
}

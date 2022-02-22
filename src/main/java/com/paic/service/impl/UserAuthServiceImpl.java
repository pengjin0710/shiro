package com.paic.service.impl;

import com.paic.model.UserAuth;
import com.paic.service.UserAuthService;
import org.springframework.stereotype.Service;

@Service
public class UserAuthServiceImpl<T> implements UserAuthService {
    @Override
    public Object auth(UserAuth user) {
        return new UserAuth("abc","123");
    }

    @Override
    public Object authSso(String sso) {
        return null;
    }
}

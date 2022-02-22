package com.paic.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

public class CustomerToken extends UsernamePasswordToken {

    private String validateCode;

    public String getValidateCode() {
        return validateCode;
    }

    public void setValidateCode(String validateCode) {
        this.validateCode = validateCode;
    }

    public CustomerToken() {
    }

    public CustomerToken(String username, String password, String validateCode) {
        super(username, password);
        this.validateCode = validateCode;
    }
}

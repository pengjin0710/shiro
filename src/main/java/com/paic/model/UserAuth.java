package com.paic.model;

/**
 * @author GBD
 */
public class UserAuth {
    /**用户名*/
    private String username;
    /**密码*/
    private String authCode;
    /**验证码*/
    private String validateCode;

    private String ssoCode;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getValidateCode() {
        return validateCode;
    }

    public void setValidateCode(String validateCode) {
        this.validateCode = validateCode;
    }

    public String getSsoCode() {
        return ssoCode;
    }

    public void setSsoCode(String ssoCode) {
        this.ssoCode = ssoCode;
    }

    public UserAuth() {
    }

    public UserAuth(String username, String authCode) {
        this.username = username;
        this.authCode = authCode;
    }

    public UserAuth(String username, String authCode, String validateCode) {
        this.username = username;
        this.authCode = authCode;
        this.validateCode = validateCode;
    }
}

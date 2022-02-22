package com.paic.filter;

import com.paic.shiro.JwtToken;
import com.paic.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtFilter extends AccessControlFilter {



    public static final String ACCESS_TOKEN = "Access-Token";

    @Override
    public String getLoginUrl() {
        return "/login";
    }

    Logger log = LoggerFactory.getLogger(JwtFilter.class);

    /**
     * 1. 返回true，shiro就直接允许访问url
     * 2. 返回false，shiro才会根据onAccessDenied的方法的返回值决定是否允许访问url
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {

        log.warn("isAccessAllowed 方法被调用");
        //这里先让它始终返回false来使用onAccessDenied()方法
        if (isLoginRequest(request, response)) {
            return true;
        }
        return false;
    }

    /**
     * 返回结果为true表明登录通过
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        log.warn("onAccessDenied 方法被调用");
        //这个地方和前端约定，要求前端将jwtToken放在请求的Header部分

        //所以以后发起请求的时候就需要在Header中放一个Authorization，值就是对应的Token
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (request.getMethod().equals(HttpMethod.OPTIONS)) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }
        String token = request.getHeader(ACCESS_TOKEN);
        if (token == null) {
            log.info("token为空");
            return false;
        }
        log.info("请求的 Header 中藏有 jwtToken {}", token);
        JwtToken jwtToken = new JwtToken(token);
        /*
         * 下面就是固定写法
         * */
        try {
            // 委托 realm 进行登录认证
            //所以这个地方最终还是调用JwtRealm进行的认证
            getSubject(servletRequest, servletResponse).login(jwtToken);
            //也就是subject.login(token)

            // 本地自动续期
            JwtUtil jwtUtil = new JwtUtil();
            Claims claims = jwtUtil.decode(token);
            long lastSetTokenTime = claims.getIssuedAt().getTime();
            long lastSetExpTime = claims.getExpiration().getTime();
            // 剩余60s自动续期
            if (lastSetExpTime - System.currentTimeMillis() < 60 * 1000) {
                token = jwtUtil.encode(5 * 60 * 1000, claims);
                response.setHeader(ACCESS_TOKEN, token);
            }
        } catch (Exception e) {
            e.printStackTrace();
            //调用下面的方法向客户端返回错误信息
            onLoginFail(servletResponse);
            return false;
        }
        return true;
        //执行方法中没有抛出异常就表示登录成功
    }

    /** 登录失败时默认返回 401 状态码*/
    private void onLoginFail(ServletResponse response) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpResponse.getWriter().write("login error");
    }
}

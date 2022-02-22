package com.paic.controller;

import com.paic.shiro.CustomerToken;
import com.paic.util.JwtUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    @Autowired
    private String expireTime;

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @GetMapping("/login")
    public ResponseEntity login(@RequestParam("name") String username ,
                                @RequestParam("password") String password ,
                                @RequestParam("validateCode") String validateCode ){
        log.info("login username:{}",username);

        Subject subject = SecurityUtils.getSubject();
        CustomerToken customerToken = new CustomerToken(username, password, validateCode);
        subject.login(customerToken);
        if(subject.isAuthenticated()){
            Map<String, Object> map = new HashMap<>(10);
            map.put("username", username);
            map.put("age", 20);

            Object principal = subject.getPrincipal();

            String token = new JwtUtil().encode(Integer.parseInt(expireTime)*1000, JwtUtil.getObjectToMap(principal));
            return ResponseEntity.ok().header("AuthToken",token).body("SUCCESS");
        }
        return new ResponseEntity("Fail",HttpStatus.UNAUTHORIZED);

    }
}

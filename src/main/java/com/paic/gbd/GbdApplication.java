package com.paic.gbd;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan({"com.paic.dao","com.gbd.dao"})
@ComponentScan(basePackages ={"com.paic","com.gbd"})
public class GbdApplication {

    public static void main(String[] args) {
        SpringApplication.run(GbdApplication.class, args);
    }

}

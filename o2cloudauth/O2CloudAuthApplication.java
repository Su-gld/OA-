package com.example.o2cloudauth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.o2cloudauth.Mapper")
public class O2CloudAuthApplication {

    public static void main(String[] args) {

        SpringApplication.run(O2CloudAuthApplication.class, args);
        System.out.println("o2cloud认证服务启动成功！");
    }

}

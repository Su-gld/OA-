package com.example.o2cloudauth.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@Tag(name = "测试接口")
public class TestController {
    @GetMapping("/hello")
    @Operation(summary = "测试接口")
    public String hello(){
        return "Hello O2Cloud!认证服务运行正常";
    }

    @GetMapping("/db-test")
    @Operation(summary = "数据库测试")
    public String dbTest(){
        return "数据库连接正常";
    }

}

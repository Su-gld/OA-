package com.example.o2cloudauth.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data

public class LoginRequest {
    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    private String captcha; //验证码，后续实现
    private String catchaKey; // 验证码key，后续实现
}

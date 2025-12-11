package com.example.o2cloudauth.Service;

import com.example.o2cloudauth.DTO.LoginRequest;
import com.example.o2cloudauth.DTO.LoginResponse;

public interface AuthService {

    /**
     * 用户登录
     */
    LoginResponse login(LoginRequest request);

    /**
     * 用户登出
     */
    void logout(String token);

    /**
     * 刷新令牌
     */
    String refreshToken(String refreshToken);

    /**
     * 获取当前登录用户信息
     */
    Object getCurrentUserInfo();
}

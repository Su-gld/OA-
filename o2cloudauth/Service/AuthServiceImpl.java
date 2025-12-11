package com.example.o2cloudauth.Service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.o2cloudauth.DTO.LoginRequest;
import com.example.o2cloudauth.DTO.LoginResponse;
import com.example.o2cloudauth.Entity.SysUser;
import com.example.o2cloudauth.Mapper.UserMapper;
import com.example.o2cloudauth.Util.JWTUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtil jwtUtil;  // 需要添加JwtUtil类

    @Override
    public LoginResponse login(LoginRequest request) {
        // 1.验证码校验（后续实现）
        // validateCaptcha(request);

        // 2.Spring Security认证
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3.获取用户信息
        SysUser user = userMapper.selectByUsername(request.getUsername());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 4.更新最后登录时间
        updateLastLoginTime(user.getId());

        // 5.生成JWT令牌
        String token = jwtUtil.generateToken(
                user.getUsername(),
                user.getId(),
                user.getNickname()
        );

        // 6.构建响应
        return LoginResponse.builder()
                .token(token)
                .userId(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .roles(getUserRoles(user.getId())) // 后续实现
                .permissions(getUserPermissions(user.getId())) // 后续实现
                .expireTime(System.currentTimeMillis() + 24 * 60 * 60 * 1000) // 24小时
                .build();
    }

    @Override
    public void logout(String token) {
        // 清除Security上下文
        SecurityContextHolder.clearContext();

        // 后续可以添加令牌黑名单机制
        log.info("用户登出：token={}", token);
    }

    @Override
    public String refreshToken(String refreshToken) {
        // 验证刷新令牌
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new RuntimeException("刷新令牌无效");
        }

        String username = jwtUtil.getUsernameFromToken(refreshToken);
        Long userId = jwtUtil.getUserIdFromToken(refreshToken);  // 修正方法名

        // 重新生成访问令牌
        return jwtUtil.generateToken(username, userId, "用户");
    }

    @Override
    public Object getCurrentUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {  // 修正逻辑
            return null;
        }

        String username = authentication.getName();
        SysUser user = userMapper.selectByUsername(username);

        if (user != null) {
            return LoginResponse.builder()
                    .userId(user.getId())
                    .username(user.getUsername())
                    .nickname(user.getNickname())
                    .avatar(user.getAvatar())  // 修正字段名
                    .roles(getUserRoles(user.getId()))
                    .build();
        }
        return null;
    }

    private void updateLastLoginTime(Long userId) {
        LambdaUpdateWrapper<SysUser> updateWrapper = new LambdaUpdateWrapper<>();  // 修正拼写
        updateWrapper
                .eq(SysUser::getId, userId)
                .set(SysUser::getLastLoginTime, LocalDateTime.now());  // 使用LocalDateTime

        userMapper.update(null, updateWrapper);  // 修正方法名
    }

    private List<String> getUserRoles(Long userId) {
        // 简化版，后续从数据库查询
        return Arrays.asList("ROLE_USER");
    }

    private List<String> getUserPermissions(Long userId) {
        // 简化版，后续从数据库查询
        return Arrays.asList("user:view", "user:edit");
    }
}

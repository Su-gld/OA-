package com.example.o2cloudauth.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.o2cloudauth.Entity.SysUser;
import com.example.o2cloudauth.Mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查询用户
        SysUser sysUser = userMapper.selectOne(
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getUsername, username)
                        .eq(SysUser::getDeleted, 0)
        );

        if (sysUser == null) {
            log.error("用户不存在: {}", username);
            throw new UsernameNotFoundException("用户名或密码错误");
        }

        // 检查用户状态
        if (sysUser.getStatus() == 0) {
            throw new RuntimeException("用户已被禁用");
        }

        // 构建权限列表（暂时返回空，后续完善）
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER")); // 默认角色

        // 返回 Spring Security 的 UserDetails
        return new User(
                sysUser.getUsername(),
                sysUser.getPassword(),
                true,   // 账号是否过期
                true,   // 凭证是否过期
                true,   // 账号是否锁定
                true,   // 是否启用
                authorities
        );
    }
}




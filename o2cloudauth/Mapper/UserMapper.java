package com.example.o2cloudauth.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.o2cloudauth.Entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<SysUser> {

    @Select("SELECT u.* FROM sys_user u " +
            "LEFT JOIN sys_user_role ur ON u.id = ur.user_id " +
            "LEFT JOIN sys_role r ON ur.role_id = r.id " +
            "WHERE u.username = #{username} AND u.deleted = 0")
    SysUser selectByUsernameWithRoles(@Param("username") String username);

    @Select("SELECT r.code FROM sys_role r " +
            "LEFT JOIN sys_user_role ur ON r.id = ur.role_id " +
            "WHERE ur.user_id = #{userId} AND r.deleted = 0")
    List<String> selectRoleCodesByUserId(@Param("userId") Long userId);

    SysUser selectByUsername(String username);
}

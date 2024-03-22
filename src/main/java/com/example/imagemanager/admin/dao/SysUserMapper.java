package com.example.imagemanager.admin.dao;

import com.example.imagemanager.admin.model.SysUserModel;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SysUserMapper {
    @Select("SELECT * FROM sys_user WHERE username = #{username} LIMIT 1")
    @Results({
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime"),
            @Result(column = "last_login_time", property = "lastLoginTime")
    })
    SysUserModel getUser(String username);

    @Update("UPDATE sys_user SET username = #{username}, create_time = #{createTime}, update_time = #{updateTime}, last_login_time = #{lastLoginTime} WHERE id = #{id}")
    void updateUser(SysUserModel userModel);

    @Select("SELECT * FROM sys_user WHERE 1 =1 ")
    @Results({
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime"),
            @Result(column = "last_login_time", property = "lastLoginTime")
    })
    List<SysUserModel> loadUsers();
}
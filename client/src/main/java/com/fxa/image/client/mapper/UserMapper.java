package com.fxa.image.client.mapper;


import com.fxa.image.client.model.User;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;

@Mapper
public interface UserMapper {


    @Select("SELECT * FROM c_user WHERE username = #{username} LIMIT 1")
    @Results({
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime"),
            @Result(column = "last_login_time", property = "lastLoginTime")
    })
    User getUser(String username);


    @Insert("insert into c_user(username,password,nickName,avatar,sex,create_time)" +
            " values(#{username},#{password},#{nickName},#{avatar},#{sex},#{createTime}) ")
    void addUser(String username,String password, String nickName, int sex, String avatar, LocalDateTime createTime);
}
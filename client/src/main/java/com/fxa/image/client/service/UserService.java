package com.fxa.image.client.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fxa.image.client.HttpResponse;
import com.fxa.image.client.mapper.UserMapper;
import com.fxa.image.client.model.LoginResult;
import com.fxa.image.client.model.Ticket;
import com.fxa.image.client.model.User;
import com.fxa.image.client.utils.SHAUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class UserService {

    private Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    UserMapper userMapper;

    @Autowired
    RedisService redisService;

    public void addUser(User user){
        userMapper.addUser(user.getUsername(), user.getPassword(), user.getNickName(), user.getSex(), user.getAvatar(), user.getCreateTime());
    }


    public HttpResponse<LoginResult> login(String username, String password) throws JsonProcessingException {
        //先从redis 中查找 在interceptor中判断是否过期
        HttpResponse<LoginResult> baseResponse = new HttpResponse<>();
        if (!StringUtils.hasText(username)) {
            baseResponse.setRet(HttpResponse.FAILED);
            baseResponse.setMsg("username empty");
            baseResponse.setErr(HttpResponse.ERR_LOGIN_USERNAME_EMPTY);
            return baseResponse;
        }
        if (!StringUtils.hasText(password)) {
            baseResponse.setMsg("password empty");
            baseResponse.setErr(HttpResponse.ERR_LOGIN_PASSWORD_ERR);
            return baseResponse;
        }
        User user = userMapper.getUser(username);

        if (user != null) {
            //这里判断如果已经登录过了 直接返回
            LoginResult loginResult = redisService.findOrGetLoginInfo(user.getId());
            if(loginResult != null){
                LOGGER.debug("login not expiry,return from cache");
                baseResponse.setRet(HttpResponse.SUCCESS);
                baseResponse.setData(loginResult);
                return baseResponse;
            }

            if (!user.getPassword().equals(password)) {
                baseResponse.setMsg("password err");
                baseResponse.setErr(HttpResponse.ERR_LOGIN_PASSWORD_ERR);
                return baseResponse;
            }
            loginResult = new LoginResult();
            loginResult.setUid(user.getId());
            loginResult.setUsername(user.getUsername());
            //创建一个登录成功的票据
            //后面放入redis中
            Ticket ticket = new Ticket();
            long current = System.currentTimeMillis();
            //当前时间加过期目标时间
            ticket.setExpiry(current + Ticket.DEFAULT_EXPIRY_DURATION);
            ticket.setLoginTime(current);
            int random = new Random().nextInt(10000000);
            String str = current + user.getId() + user.getUsername() + random;
            String token = SHAUtils.getSHA256Hash(str);
            if (token == null || token.isEmpty()) {
                LOGGER.debug("login fail,token create failed");
                baseResponse.setRet(HttpResponse.FAILED);
                baseResponse.setErr(HttpResponse.ERR_LOGIN_TOKEN_CREATE_ERR);
                baseResponse.setMsg("login fail,token create failed");
                baseResponse.setErr(HttpResponse.ERR_LOGIN_USER_NOT_FOUND);
                return baseResponse;
            }
            LOGGER.debug("login suc,token:" + token);
            ticket.setToken(token);
            loginResult.setTicket(ticket);
            user.setLastLoginTime(LocalDateTime.now());
//            updateUser(user);
            baseResponse.setRet(HttpResponse.SUCCESS);
            baseResponse.setData(loginResult);
            //登录信息放入redis中
            redisService.saveLoginInfoToHash(loginResult.getUid(),loginResult);
        } else {
            baseResponse.setMsg("user not found");
            baseResponse.setErr(HttpResponse.ERR_LOGIN_USER_NOT_FOUND);
            return baseResponse;
        }
        return baseResponse;
    }
}

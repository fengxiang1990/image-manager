package com.example.imagemanager.admin.service;

import com.example.imagemanager.admin.dao.SysUserMapper;
import com.example.imagemanager.admin.model.LoginResult;
import com.example.imagemanager.admin.model.SysUserModel;
import com.example.imagemanager.admin.model.Ticket;
import com.example.imagemanager.cache.SysUserCache;
import com.example.imagemanager.model.BaseResponse;
import com.example.imagemanager.utils.JsonUtils;
import com.example.imagemanager.utils.SHAUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class SysUserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SysUserService.class);


    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public List<SysUserModel> getSysUsers(){
        return userMapper.loadUsers();
    }

    public SysUserModel getUserByUserName(String username) {
        return userMapper.getUser(username);
    }

    //登录 不使用spring security
    public BaseResponse<LoginResult> login(String username, String password) throws JsonProcessingException {
        //先从redis 中查找 在interceptor中判断是否过期
        SysUserModel sysUserModel = userMapper.getUser(username);
        BaseResponse<LoginResult> baseResponse = new BaseResponse<>();

        if (!StringUtils.hasText(username)) {
            baseResponse.setRet(BaseResponse.FAILED);
            baseResponse.setMsg("username empty");
            baseResponse.setErr(BaseResponse.ERR_LOGIN_USERNAME_EMPTY);
            return baseResponse;
        }
        if (!StringUtils.hasText(password)) {
            baseResponse.setMsg("password empty");
            baseResponse.setErr(BaseResponse.ERR_LOGIN_PASSWORD_ERR);
            return baseResponse;
        }

        if (sysUserModel != null) {
            if (!sysUserModel.getPassword().equals(password)) {
                baseResponse.setMsg("password err");
                baseResponse.setErr(BaseResponse.ERR_LOGIN_PASSWORD_ERR);
                return baseResponse;
            }
            LoginResult loginResult = new LoginResult();
            loginResult.setUid(sysUserModel.getId());
            loginResult.setUsername(sysUserModel.getUsername());
            //创建一个登录成功的票据
            //后面放入redis中
            Ticket ticket = new Ticket();
            long current = System.currentTimeMillis();
            //当前时间加过期目标时间
            ticket.setExpiry(current + Ticket.DEFAULT_EXPIRY_DURATION);
            ticket.setLoginTime(current);
            int random = new Random().nextInt(10000000);
            String str = current + sysUserModel.getId() + sysUserModel.getUsername() + random;
            String token = SHAUtils.getSHA256Hash(str);
            if (token == null || token.isEmpty()) {
                LOGGER.debug("login fail,token create failed");
                baseResponse.setRet(BaseResponse.FAILED);
                baseResponse.setErr(BaseResponse.ERR_LOGIN_TOKEN_CREATE_ERR);
                baseResponse.setMsg("login fail,token create failed");
                baseResponse.setErr(BaseResponse.ERR_LOGIN_USER_NOT_FOUND);
                return baseResponse;
            }
            LOGGER.debug("login suc,token:" + token);
            ticket.setToken(token);
            loginResult.setTicket(ticket);
            sysUserModel.setLastLoginTime(LocalDateTime.now());
            updateUser(sysUserModel);
            baseResponse.setRet(BaseResponse.SUCCESS);
            baseResponse.setData(loginResult);
            //登录信息放入redis中
            Object obj = redisTemplate.opsForValue().get(sysUserModel.getId());
            SysUserCache sysUserCache;
            if (obj instanceof String) {
                sysUserCache = JsonUtils.fromJson((String) obj, SysUserCache.class);
                sysUserCache.setTicket(ticket);
            }else{
                sysUserCache = new SysUserCache();
                sysUserCache.setTicket(ticket);
            }
            redisTemplate.opsForValue().set(sysUserModel.getId(), JsonUtils.toJson(sysUserCache));
        } else {
            baseResponse.setMsg("user not found");
            baseResponse.setErr(BaseResponse.ERR_LOGIN_USER_NOT_FOUND);
            return baseResponse;
        }
        return baseResponse;
    }

    public void updateUser(SysUserModel userModel) {
        userMapper.updateUser(userModel);
    }

    public void updateUserLastLoginTime(String username, LocalDateTime now) {
        SysUserModel sysUserModel = userMapper.getUser(username);
        sysUserModel.setLastLoginTime(now);
        updateUser(sysUserModel);
    }
}

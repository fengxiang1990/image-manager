package com.fxa.image.client.inteceptor;

import com.fxa.image.client.HttpResponse;
import com.fxa.image.client.RedisConst;
import com.fxa.image.client.model.LoginResult;
import com.fxa.image.client.model.Ticket;
import com.fxa.image.client.utils.JsonUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    private static Logger LOGGER = LoggerFactory.getLogger(LoginInterceptor.class);

    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try{
            String uid = request.getHeader("uid");
            LOGGER.debug("uid:"+uid);
            //uid 存在 去缓存中检查token 是否有效
            //先检查大的KEY
            boolean exists = Boolean.TRUE.equals(redisTemplate.hasKey(RedisConst.KEY_USER_LOGIN_MAP));
            if(exists){
                //再检查大key里面存的登录信息hash表
                exists = Boolean.TRUE.equals(redisTemplate.opsForHash().hasKey(RedisConst.KEY_USER_LOGIN_MAP,uid));
            }
            if(exists){
                LoginResult sysUserCache = (LoginResult) redisTemplate.opsForHash().get(RedisConst.KEY_USER_LOGIN_MAP,uid);
                if(sysUserCache != null){
                    Ticket ticket = sysUserCache.getTicket();
                    if(ticket != null){
                        boolean expiry = System.currentTimeMillis() > ticket.expiry;
                        if(expiry){
                            HttpResponse<LoginResult> baseResponse = new HttpResponse<>();
                            baseResponse.setRet(HttpResponse.FAILED);
                            baseResponse.setErr(HttpResponse.ERR_LOGIN_TOKEN_EXPIRY);
                            baseResponse.setMsg("login expiry");
                            response.getWriter().println(JsonUtils.toJson(baseResponse));
                            //clear user in redis
                            boolean delete = Boolean.TRUE.equals(redisTemplate.delete(uid));
                            LOGGER.debug("delete expiry user in redis:"+delete);
                            return false;
                        }
                    }
                }
            }
            return true;
        }catch (Exception e){
            HttpResponse<LoginResult> baseResponse = new HttpResponse<>();
            baseResponse.setRet(HttpResponse.FAILED);
            baseResponse.setErr(HttpResponse.ERR_LOGIN_NONE);
            baseResponse.setMsg("not login");
            response.getWriter().println(JsonUtils.toJson(baseResponse));
        }
        return false;
    }
}

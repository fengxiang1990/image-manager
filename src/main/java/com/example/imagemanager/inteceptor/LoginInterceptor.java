package com.example.imagemanager.inteceptor;

import com.example.imagemanager.admin.model.LoginResult;
import com.example.imagemanager.admin.model.Ticket;
import com.example.imagemanager.cache.SysUserCache;
import com.example.imagemanager.model.BaseResponse;
import com.example.imagemanager.utils.JsonUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {


    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("preHandle");
        try{
            String uid = request.getHeader("uid");
            System.out.println("uid:"+uid);
            //uid 存在 去缓存中检查token 是否有效
            boolean exists = Boolean.TRUE.equals(redisTemplate.hasKey(uid));
            if(exists){
                String value = (String) redisTemplate.opsForValue().get(uid);
                SysUserCache sysUserCache = JsonUtils.fromJson(value,SysUserCache.class);
                if(sysUserCache != null){
                    Ticket ticket = sysUserCache.getTicket();
                    if(ticket != null){
                        boolean expiry = System.currentTimeMillis() > ticket.expiry;
                        if(expiry){
                            BaseResponse<LoginResult> baseResponse = new BaseResponse<>();
                            baseResponse.setRet(BaseResponse.FAILED);
                            baseResponse.setErr(BaseResponse.ERR_LOGIN_TOKEN_EXPIRY);
                            baseResponse.setMsg("login expiry");
                            response.getWriter().println(JsonUtils.toJson(baseResponse));
                            //clear user in redis
                            boolean delete = Boolean.TRUE.equals(redisTemplate.delete(uid));
                            System.out.println("delete expiry user in redis:"+delete);
                            return false;
                        }
                    }
                }
            }
            return true;
        }catch (Exception e){
            BaseResponse<LoginResult> baseResponse = new BaseResponse<>();
            baseResponse.setRet(BaseResponse.FAILED);
            baseResponse.setErr(BaseResponse.ERR_LOGIN_NONE);
            baseResponse.setMsg("not login");
            response.getWriter().println(JsonUtils.toJson(baseResponse));
        }
        return false;
    }
}

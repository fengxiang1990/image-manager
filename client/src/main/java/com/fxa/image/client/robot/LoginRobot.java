package com.fxa.image.client.robot;

import com.fxa.image.client.RedisConst;
import com.fxa.image.client.service.RedisService;
import com.fxa.image.client.service.UserService;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class LoginRobot extends BaseRobot{

    private Logger LOGGER = LoggerFactory.getLogger(LoginRobot.class);

    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    @Autowired
    RedisService redisService;

    @Autowired
    UserService userService;

    @Value("${robot.login.duration}")
    int duration;

    @Value("${robot.login.count}")
    int count;


    @Override
    public void execute() {
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    //windows 不支持randomKeys
//                    List<Object> users = redisTemplate.opsForHash().randomKeys(RedisConst.KEY_USER_PWD_MAP, 50);
                    List<Object> userList = redisService.getRandomHashKeys(RedisConst.KEY_USER_PWD_MAP,count);
                    if (userList == null || userList.isEmpty()) {
                        return;
                    }
                    //从缓存中拿到用户密码列表 再判断登录缓存中是否存在用户
                    for (Object username : userList) {
                        //login
                        Object pwd = redisTemplate.opsForHash().get(RedisConst.KEY_USER_PWD_MAP, username);
                        login((String) username, (String) pwd);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        },0,duration, TimeUnit.SECONDS);

    }

    private void login(String userName,String pwd){
        try {
//            userService.login(userName, pwd);
            //使用okhttp来模拟登录
            HttpUrl url = new HttpUrl.Builder()
                    .scheme("http")
                    .host(BASE_URL)
                    .port(8082)
                    .addPathSegment("login")
                    .build();
            String json = "{\"username\":\""+userName+"\", \"password\":\""+pwd+"\"}";

            // 创建一个RequestBody对象，将JSON字符串作为请求体
            RequestBody body = RequestBody.create(json,MediaType.parse("application/json"));

            Request request = new Request.Builder().url(url)
                    .addHeader("Content-Type","application/json;charset=utf-8")
                    .addHeader("Accept","application/json")
                    .method("POST",body)
                    .build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    LOGGER.debug("onFailure:"+e.getMessage());
                    call.cancel();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    LOGGER.debug("onResponse:"+(response.code() == 200 ? "success" : "failed"));
                    if(response.code() != 200){
                        call.cancel();
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

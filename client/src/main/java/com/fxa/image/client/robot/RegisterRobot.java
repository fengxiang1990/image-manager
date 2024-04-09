package com.fxa.image.client.robot;

import com.fxa.image.client.model.User;
import com.fxa.image.client.service.UserService;
import com.fxa.image.client.utils.PwdStringGenerator;
import com.fxa.image.client.utils.RandomChineseCharacterGenerator;
import com.fxa.image.client.utils.RandomChineseNicknameGenerator;
import com.fxa.image.client.utils.SHAUtils;
import com.fxa.image.common.RedisConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * 注册用户机器人
 */
@Component
public class RegisterRobot extends BaseRobot{

    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    @Autowired
    UserService userService;

    @Value("${robot.reigister.duration}")
    int duration;

    @Override
    public void execute() {
        System.out.println("RegisterRobot:"+duration);
        //定时干活
        executor.scheduleAtFixedRate(new MyRunnable(userService,redisTemplate),0,duration, TimeUnit.SECONDS);
    }

    static class MyRunnable implements Runnable{

        UserService userService;
        RedisTemplate<String,Object> redisTemplate;
        public MyRunnable(UserService userService,RedisTemplate<String,Object> redisTemplate){
            this.userService = userService;
            this.redisTemplate = redisTemplate;
        }
        @Override
        public void run() {
            try{
                User user = new User();
                String userName = "u_robot_"+PwdStringGenerator.generateUserNameRandomString(9)+"_"+System.currentTimeMillis();
                String pwd = SHAUtils.getSHA256Hash(PwdStringGenerator.generateRandomString(12));
                String nickName = RandomChineseNicknameGenerator.generateRandomChineseNickname(RandomChineseCharacterGenerator.generateRandomChineseCharacters());
                user.setUsername(userName);
                user.setPassword(pwd);
                user.setNickName(nickName);
                user.setAvatar("");
                user.setSex(System.currentTimeMillis() % 2 == 0 ? User.sex_male : User.sex_female);
                user.setCreateTime(LocalDateTime.now());
                userService.addUser(user);
                redisTemplate.opsForHash().put(RedisConst.KEY_USER_PWD_MAP,userName,pwd);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}

package com.fxa.image.client.service;


import com.fxa.image.client.RedisConst;
import com.fxa.image.client.model.LoginResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class RedisService {


    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public LoginResult findOrGetLoginInfo(String uid){
        Object obj = redisTemplate.opsForHash().get(RedisConst.KEY_USER_LOGIN_MAP,uid);
        if(obj instanceof LoginResult){
            return (LoginResult) obj;
        }
        return null;
    }

    public void saveLoginInfoToHash(String uid,Object object){
        redisTemplate.opsForHash().put(RedisConst.KEY_USER_LOGIN_MAP,uid,object);
    }

    public List<Object> getRandomHashKeys(String hashKey, int count) {
        HashOperations<String, Object, Object> hashOps = redisTemplate.opsForHash();
        Set<Object> fields = hashOps.keys(hashKey);

        if (fields.size() < count) {
            count = fields.size();
        }
        //随机取
        List<Object> objects = new ArrayList<>(fields);
        Collections.shuffle(objects);
        List<Object> randomFields = new ArrayList<>();
        for(int i = 0;i < count;i++){
            randomFields.add(objects.get(i));
        }
        return randomFields;
    }

}

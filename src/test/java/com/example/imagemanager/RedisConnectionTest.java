package com.example.imagemanager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class RedisConnectionTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void testRedisConnection() {
        // 尝试与Redis进行交互
        redisTemplate.opsForValue().set("testKey", "testValue");
        Object value = redisTemplate.opsForValue().get("testKey");

        // 验证连接是否成功
        assertTrue(value != null && value.equals("testValue"));
    }
}
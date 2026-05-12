package net.abdullahjutt.journalApp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class RedisTests {
    @Autowired
    private RedisTemplate redisTemplate;
    @Test
    void testRedis(){
        redisTemplate.opsForValue().set("email","abdullah@gmail.com");
         Object email = redisTemplate.opsForValue().get("email");
         int a=1;

    }
}

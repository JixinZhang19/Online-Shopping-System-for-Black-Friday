package com.skillup.infrastructure.redis;

import com.alibaba.fastjson2.JSON;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class RedisRepoTest {
    @Autowired
    RedisRepo redisRepo;

    public static String KEY = "name";
    public static String VALUE = "skillupit";

    @AfterEach
    public void cleanDateCreate() {
        // clean data
        redisRepo.set(KEY, "Clean it!");
        System.out.println("---Clean Data ----");
    }
    @Test
    public void setAndGetValueTest() {
        redisRepo.set(KEY, VALUE);
        System.out.println(redisRepo.get(KEY));
        // Json转换为String类型
        assertEquals(VALUE, JSON.parseObject(redisRepo.get(KEY), String.class));
        // redisRepo.delete(KEY);
        // assertEquals(null, JSON.parseObject(redisRepo.get(KEY), String.class));
    }

}
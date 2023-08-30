package com.skillup.infrastructure.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

@Configuration
public class LockStockScript {
    @Bean
    DefaultRedisScript<Long> redisLockStockScript() {
        // lua script return Long
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        // 1) set lua script source
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("redis/lockStock.lua")));
        // 2) return type must be in: [Long, Boolean, List, deserialized value]
        redisScript.setResultType(Long.class);

        return redisScript;
    }
}

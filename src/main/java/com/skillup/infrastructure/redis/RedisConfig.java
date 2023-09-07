package com.skillup.infrastructure.redis;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class RedisConfig {
    /**
     * LocalDateTime 转化的坑太多，
     * 1. Jackson2JsonRedisSerializer 会转化 LocalDateTime 成数组【2022， 12， 31， 00，00，00】，
     *    Java 8不支持LocalDateTime需要添加com.fasterxml.jackson.datatype:jackson-datatype-jsr310依
     * 2. StringRedisSerializer() 作为 ValueSerializer 将RedisTemplate<Key, Value>中的 Value 保存成 String
     *    这里 RedisTemplate<KEY, VALUE> 中的value我们保存 FastJson2的JsonString，每次set，get调用FastJson2序列化/反序列化
     *    FastJson2 会帮我们转化 LocalDateTime 为 String "2022-12-31T00:00:00"，
     */
    @Bean
    RedisTemplate<String, String> redisTemplate(RedisConnectionFactory redisConnectionFactory) {

        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        // 链接redis服务区
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 设置值（value）的序列化（serialize）不采用Jackson2JsonRedisSerializer，使用一般的StringRedisSerializer。
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        // 设置键（key）的序列化采用StringRedisSerializer。
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
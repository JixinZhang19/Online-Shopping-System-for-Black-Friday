package com.skillup.infrastructure.redis;

import com.alibaba.fastjson2.JSON;
import com.skillup.domain.promotionCache.PromotionCacheDomain;
import com.skillup.domain.promotionCache.PromotionCacheRepository;
import com.skillup.domain.stockCache.StockCacheDomain;
import com.skillup.domain.stockCache.StockCacheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Repository;


import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

@Repository
public class RedisRepo implements StockCacheRepository, PromotionCacheRepository {

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Autowired
    DefaultRedisScript<Long> redisLockStockScript;

    @Autowired
    DefaultRedisScript<Long> redisRevertStockScript;

    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, JSON.toJSONString(value));
    }

    public String get(String key) {
        if (Objects.isNull(key)) return null;
        return redisTemplate.opsForValue().get(key);
    }

    // delete 不太对，后续可以自己实现一下
    /*
    public void delete(String key){
        if (Objects.isNull(key)) return;
        redisTemplate.opsForValue().getAndDelete(key);
    }
    */

    @Override
    public Long getPromotionAvailableStock(String promotionId) {
        // create stock key
        String key = StockCacheDomain.createStockKey(promotionId);
        // get stock by key
        return JSON.parseObject(get(key), Long.class);
    }

    @Override
    public void setPromotionAvailableStock(String promotionId, Long availableStock) {
        // create stock key
        String key = StockCacheDomain.createStockKey(promotionId);
        // set {key, stock}
        set(key, availableStock);
    }

    @Override
    public boolean lockAvailableStock(StockCacheDomain stockCacheDomain) {
        // 0 Lua script to ACID below operations
        // 1 select form available_stock = ?
        // 2 if available_stock > 0 then update available_stock = available_stock - 1
        try {
            /*
            Long stock = redisTemplate.execute(redisLockStockScript,
                    Collections.singletonList(
                            stockCacheDomain.createStockKey(stockCacheDomain.getPromotionId())
                    ));

             */
            Long stock = redisTemplate.execute(redisLockStockScript,
                    Arrays.asList(
                            stockCacheDomain.createStockKey(stockCacheDomain.getPromotionId()),
                            stockCacheDomain.getOrderId().toString(),
                            stockCacheDomain.getOperationName().toString()
                    ));
            if (stock >= 0) {
                return true;
            } else {
                // -1 means sold out, -2 means promotion doesn't exist
                return false;
            }
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    @Override
    public boolean revertAvailableStock(StockCacheDomain stockCacheDomain) {
        // 0 Lua script to ACID below operations
        // 1 select form available_stock = ?
        // 2 if available_stock > 0 then update available_stock = available_stock - 1
        try {
            /*
            Long stock = redisTemplate.execute(redisRevertStockScript,
                    Collections.singletonList(
                            stockCacheDomain.createStockKey(stockCacheDomain.getPromotionId())
                    ));
             */
            Long stock = redisTemplate.execute(redisRevertStockScript,
                    Arrays.asList(
                            stockCacheDomain.createStockKey(stockCacheDomain.getPromotionId()),
                            stockCacheDomain.getOrderId().toString(),
                            stockCacheDomain.getOperationName().toString()
                    ));
            if (stock > 0) {
                return true;
            } else {
                // -1 means sold out, -2 means promotion doesn't exist
                return false;
            }
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    @Override
    public PromotionCacheDomain getPromotionById(String id) {
        return JSON.parseObject(get(id), PromotionCacheDomain.class);
    }

    @Override
    public void setPromotion(PromotionCacheDomain promotionCacheDomain) {
        set(promotionCacheDomain.getPromotionId(), promotionCacheDomain);
    }
}
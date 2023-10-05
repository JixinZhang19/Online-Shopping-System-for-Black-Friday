package com.skillup.application.promotion;

import com.skillup.application.mapper.PromotionMapper;
import com.skillup.domain.promotion.PromotionDomain;
import com.skillup.domain.promotion.PromotionService;
import com.skillup.domain.promotionCache.PromotionCacheDomain;
import com.skillup.domain.promotionCache.PromotionCacheService;
import com.skillup.domain.stockCache.StockCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class PromotionApplication {
    @Autowired
    StockCacheService stockCacheService;

    @Autowired
    PromotionCacheService promotionCacheService;

    @Autowired
    PromotionService promotionService;

    public PromotionDomain getById(String id) {
        // cache aside read strategy

        // 1. get promotion cache domain
        // 1.1 try to hit cache
        PromotionCacheDomain cacheDomain = promotionCacheService.getPromotionById(id);
        // 1.2 if not hit: read database and then insert cache
        if (Objects.isNull(cacheDomain)) {
            PromotionDomain promotionDomain = promotionService.getById(id);
            if (Objects.isNull(promotionDomain)) {
                return null;
            }
            // insert cache
            cacheDomain = PromotionMapper.INSTANCE.toCacheDomain(promotionDomain);
            promotionCacheService.setPromotion(cacheDomain);
        }

        // 2. get stock cache
        // 2.1 try to hit cache
        Long availableStock = stockCacheService.getAvailableStock(id);
        // 2.2 if not hit: read database and then insert cache
        if (Objects.isNull(availableStock)) {
            // throw new RuntimeException();
            PromotionDomain promotionDomain = promotionService.getById(id);
            if (Objects.isNull(promotionDomain)) {
                return null;
            }
            // insert cache
            availableStock = promotionDomain.getAvailableStock();
            stockCacheService.setAvailableStock(promotionDomain.getPromotionId(), availableStock);
        }

        // 3. update stock onto available stock in promotion
        cacheDomain.setAvailableStock(availableStock);
        return PromotionMapper.INSTANCE.toDomain(cacheDomain);
    }


}

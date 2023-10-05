package com.skillup.application.promotion;

import com.skillup.application.mapper.PromotionMapper;
import com.skillup.domain.promotion.PromotionDomain;
import com.skillup.domain.promotion.PromotionService;
import com.skillup.domain.promotionCache.PromotionCacheDomain;
import com.skillup.domain.promotionCache.PromotionCacheService;
import com.skillup.domain.stockCache.StockCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PromotionPreHeatApplication implements ApplicationRunner {
    @Autowired
    PromotionService promotionService;

    @Autowired
    StockCacheService stockCacheService;

    @Autowired
    PromotionCacheService promotionCacheService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("------ Cache preheat: init active promotion into cache. ------");
        // 1. search active promotions
        List<PromotionDomain> activePromotions = promotionService.getByStatus(1);
        activePromotions.forEach(promotionDomain -> {
            // 2. set available stock to cache
            stockCacheService.setAvailableStock(promotionDomain.getPromotionId(), promotionDomain.getAvailableStock());
            // 3. set promotion cache domain to cache
            promotionCacheService.setPromotion(PromotionMapper.INSTANCE.toCacheDomain(promotionDomain));
        });
    }

}

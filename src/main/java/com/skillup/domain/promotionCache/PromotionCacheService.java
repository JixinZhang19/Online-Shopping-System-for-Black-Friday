package com.skillup.domain.promotionCache;

import com.skillup.domain.promotion.PromotionDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PromotionCacheService {
    @Autowired
    PromotionCacheRepository promotionCacheRepository;

    public PromotionCacheDomain getPromotionById(String id) {
        return promotionCacheRepository.getPromotionById(id);
    }

    public PromotionCacheDomain setPromotion(PromotionCacheDomain promotionCacheDomain) {
        promotionCacheRepository.setPromotion(promotionCacheDomain);
        return promotionCacheDomain;
    }
}

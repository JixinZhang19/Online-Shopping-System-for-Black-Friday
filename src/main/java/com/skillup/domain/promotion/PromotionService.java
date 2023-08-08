package com.skillup.domain.promotion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PromotionService {
    @Autowired
    PromotionRepository promotionRepository;

    public PromotionDomain createPromotion(PromotionDomain domain) {
        promotionRepository.createPromotion(domain);
        return domain;
    }
}

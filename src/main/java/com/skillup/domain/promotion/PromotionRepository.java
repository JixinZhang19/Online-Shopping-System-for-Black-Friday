package com.skillup.domain.promotion;

import com.skillup.domain.promotion.PromotionDomain;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PromotionRepository {
    void createPromotion(PromotionDomain domain);

    PromotionDomain getById(String id);

    List<PromotionDomain> getByStatus(Integer status);

    void updatePromotion(PromotionDomain promotionDomain);

}
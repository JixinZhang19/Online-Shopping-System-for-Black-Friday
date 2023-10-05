package com.skillup.domain.promotion.stockStrategy;

import com.skillup.domain.promotion.PromotionDomain;
import com.skillup.domain.promotion.PromotionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "oversell")
@Slf4j
public class OverSellStrategy implements StockOperation {

    @Autowired
    PromotionRepository promotionRepository;

    @Override
    public boolean lockStock(String id) {
        log.info("----------- Over Sell Strategy -----------");
        // 1. get available stock
        PromotionDomain currentPromotionDomain = promotionRepository.getById(id);
        // 1.1 check > 0
        if (currentPromotionDomain.getAvailableStock() <= 0) {
            return false;
        }
        log.info("------------ Current available stock is: {} ------------", currentPromotionDomain.getAvailableStock());
        // 2. set available stock - 1, set lock stock + 1
        currentPromotionDomain.setAvailableStock(currentPromotionDomain.getAvailableStock()-1);
        currentPromotionDomain.setLockStock(currentPromotionDomain.getLockStock()+1);
        log.info("------------ Update available stock to: {}, lock stock to: {} ------------", currentPromotionDomain.getAvailableStock(), currentPromotionDomain.getLockStock());
        // 3. save domain
        promotionRepository.updatePromotion(currentPromotionDomain);
        return true;
    }

    @Override
    public boolean revertStock(String id) {
        return false;
    }

    @Override
    public boolean deductStock(String id) {
        return false;
    }


}

package com.skillup.domain.promotion;

import com.skillup.domain.promotion.stockStrategy.StockOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class PromotionService {
    @Autowired
    PromotionRepository promotionRepository;

    @Resource(name = "${promotion.stock-strategy}")
    StockOperation stockOperation;

    public PromotionDomain createPromotion(PromotionDomain domain) {
        promotionRepository.createPromotion(domain);
        return domain;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public PromotionDomain getById(String id) {
        return promotionRepository.getById(id);
    }

    public List<PromotionDomain> getByStatus(Integer status) {
        return promotionRepository.getByStatus(status);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public boolean lockStock(String id) {
        return stockOperation.lockStock(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public boolean revertStock(String id) {
        return stockOperation.revertStock(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public boolean deductStock(String id) {
        return stockOperation.deductStock(id);
    }
}

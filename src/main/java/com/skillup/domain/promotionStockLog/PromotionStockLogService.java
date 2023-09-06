package com.skillup.domain.promotionStockLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PromotionStockLogService {

    @Autowired
    PromotionStockLogRepository promotionStockLogRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    public PromotionStockLogDomain createPromotionLog(PromotionStockLogDomain domain) {
        promotionStockLogRepository.createPromotionLog(domain);
        return domain;
    }

    public PromotionStockLogDomain getPromotionLogByOrderIdAndOperation(Long orderId, String operationName) {
        return promotionStockLogRepository.getPromotionLogByOrderIdAndOperation(orderId, operationName);
    }
}
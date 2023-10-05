package com.skillup.domain.promotionStockLog;

public interface PromotionStockLogRepository {

    void createPromotionLog(PromotionStockLogDomain promotionLogDomain);

    PromotionStockLogDomain getPromotionLogByOrderIdAndOperation(Long orderId, String operationName);

}

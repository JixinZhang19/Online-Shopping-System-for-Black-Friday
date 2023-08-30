package com.skillup.domain.stock;

public interface StockRepository {
    Long getPromotionAvailableStock(String promotionId);

    void setPromotionAvailableStock(String promotionId, Long availableStock);

    boolean lockAvailableStock(StockDomain stockDomain);

    boolean revertAvailableStock(StockDomain stockDomain);
}

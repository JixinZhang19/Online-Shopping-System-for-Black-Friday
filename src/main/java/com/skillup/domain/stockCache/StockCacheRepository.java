package com.skillup.domain.stockCache;

public interface StockCacheRepository {
    Long getPromotionAvailableStock(String promotionId);

    void setPromotionAvailableStock(String promotionId, Long availableStock);

    boolean lockAvailableStock(StockCacheDomain stockCacheDomain);

    boolean revertAvailableStock(StockCacheDomain stockCacheDomain);
}

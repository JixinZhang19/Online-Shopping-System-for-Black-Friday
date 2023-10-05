package com.skillup.domain.stockCache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockCacheService {
    @Autowired
    StockCacheRepository stockCacheRepository;

    public Long getAvailableStock(String promotionId) {
        return stockCacheRepository.getPromotionAvailableStock(promotionId);
    }

    public void setAvailableStock(String promotionId, Long availableStock) {
        stockCacheRepository.setPromotionAvailableStock(promotionId, availableStock);
    }

    public boolean lockStock(StockCacheDomain stockCacheDomain) {
        return stockCacheRepository.lockAvailableStock(stockCacheDomain);
    }

    public boolean revertStock(StockCacheDomain stockCacheDomain) {
        return stockCacheRepository.revertAvailableStock(stockCacheDomain);
    }

}

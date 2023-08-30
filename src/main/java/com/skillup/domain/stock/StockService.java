package com.skillup.domain.stock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockService {
    @Autowired
    StockRepository stockRepository;

    public Long getAvailableStock(String promotionId) {
        return stockRepository.getPromotionAvailableStock(promotionId);
    }

    public void setAvailableStock(String promotionId, Long availableStock) {
        stockRepository.setPromotionAvailableStock(promotionId, availableStock);
    }

    public boolean lockStock(StockDomain stockDomain) {
        return stockRepository.lockAvailableStock(stockDomain);
    }

    public boolean revertStock(StockDomain stockDomain) {
        return stockRepository.revertAvailableStock(stockDomain);
    }

}

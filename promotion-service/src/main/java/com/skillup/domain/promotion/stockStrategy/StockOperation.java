package com.skillup.domain.promotion.stockStrategy;

public interface StockOperation {
    boolean lockStock(String id);

    boolean revertStock(String id);

    boolean deductStock(String id);
}

package com.skillup.domain.stock;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class StockDomain {
    private String promotionId;
    private String availableStock;

    public static final String PROMOTION_PREFIX = "PROMOTION_";

    public static final String STOCK_SUFFIX = "_STOCK";

    public static String createStockKey(String promotionId) {
        return  PROMOTION_PREFIX + promotionId + STOCK_SUFFIX;
    }
}

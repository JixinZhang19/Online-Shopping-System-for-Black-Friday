package com.skillup.domain.stockCache;

import com.skillup.domain.util.OperationName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockCacheDomain {

    private String promotionId;

    private Long orderId;

    private OperationName operationName;

    private String availableStock;

    public static final String PROMOTION_PREFIX = "PROMOTION_";

    public static final String STOCK_SUFFIX = "_STOCK";

    public static String createStockKey(String promotionId) {
        return  PROMOTION_PREFIX + promotionId + STOCK_SUFFIX;
    }
}

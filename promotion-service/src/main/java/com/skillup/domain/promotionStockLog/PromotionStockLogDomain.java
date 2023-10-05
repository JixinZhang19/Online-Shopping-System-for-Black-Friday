package com.skillup.domain.promotionStockLog;

import com.skillup.domain.util.OperationName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PromotionStockLogDomain {

    private String userId;

    private Long orderNumber;

    private String promotionId;

    private OperationName operationName;

    private LocalDateTime createTime;

}
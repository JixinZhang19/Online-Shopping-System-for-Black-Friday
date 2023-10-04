package com.skillup.application.promotion.handler;

import com.skillup.application.promotion.event.DeductStockEvent;
import com.skillup.application.promotion.event.LockStockEvent;
import com.skillup.domain.order.OrderDomain;
import com.skillup.domain.promotion.PromotionService;
import com.skillup.domain.promotionStockLog.PromotionStockLogDomain;
import com.skillup.domain.promotionStockLog.PromotionStockLogService;
import com.skillup.domain.util.OperationName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

public class DeductStockHandler implements ApplicationListener<DeductStockEvent> {

    @Autowired
    PromotionService promotionService;

    @Autowired
    PromotionStockLogService promotionStockLogService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void onApplicationEvent(DeductStockEvent event) {

        OrderDomain orderDomain = event.orderDomain;

        // ※ keep idempotent: use promotion log, if already exists, return directly
        PromotionStockLogDomain promotionStockLog = promotionStockLogService.getPromotionLogByOrderIdAndOperation(orderDomain.getOrderNumber(), OperationName.DEDUCT_STOCK.toString());
        if (Objects.nonNull(promotionStockLog)) {
            return;
        }

        // deduct stock to database
        promotionService.deductStock(orderDomain.getPromotionId());

        // add promotion log
        promotionStockLogService.createPromotionLog(toPromotionStockLogDomain(orderDomain));
    }

    private PromotionStockLogDomain toPromotionStockLogDomain(OrderDomain orderDomain) {
        return PromotionStockLogDomain.builder()
                .createTime(LocalDateTime.now())
                .promotionId(orderDomain.getPromotionId())
                .orderNumber(orderDomain.getOrderNumber())
                .operationName(OperationName.DEDUCT_STOCK)
                .userId(orderDomain.getUserId())
                .build();
    }
}

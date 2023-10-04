package com.skillup.application.promotion.handler;

import com.skillup.application.promotion.event.LockStockEvent;
import com.skillup.application.promotion.event.RevertStockEvent;
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

public class RevertStockHandler implements ApplicationListener<RevertStockEvent> {

    @Autowired
    PromotionService promotionService;

    @Autowired
    PromotionStockLogService promotionStockLogService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void onApplicationEvent(RevertStockEvent event) {

        OrderDomain orderDomain = event.orderDomain;

        // ※ keep idempotent: use promotion log, if already exists, return directly
        PromotionStockLogDomain promotionStockLog = promotionStockLogService.getPromotionLogByOrderIdAndOperation(orderDomain.getOrderNumber(), OperationName.REVERT_STOCK.toString());
        if (Objects.nonNull(promotionStockLog)) {
            return;
        }

        // revert stock to database
        promotionService.revertStock(orderDomain.getPromotionId());

        // add promotion log
        promotionStockLogService.createPromotionLog(toPromotionStockLogDomain(orderDomain));

    }

    private PromotionStockLogDomain toPromotionStockLogDomain(OrderDomain orderDomain) {
        return PromotionStockLogDomain.builder()
                .createTime(LocalDateTime.now())
                .promotionId(orderDomain.getPromotionId())
                .orderNumber(orderDomain.getOrderNumber())
                .operationName(OperationName.REVERT_STOCK)
                .userId(orderDomain.getUserId())
                .build();
    }
}

package com.skillup.infrastructure.rocketMq.consumer;

import com.alibaba.fastjson2.JSON;
import com.skillup.application.promotion.event.DeductStockEvent;
import com.skillup.domain.order.OrderDomain;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

@Component
@RocketMQMessageListener(topic = "${promotion.topic.deduct-stock}", consumerGroup = "${promotion.topic.deduct-stock-group}")
public class DeductStockConsumer implements RocketMQListener<MessageExt> {

    /*
    @Autowired
    PromotionService promotionService;

    @Autowired
    PromotionStockLogService promotionStockLogService;

     */

    @Autowired
    ApplicationContext applicationContext;

    @Override
    @Transactional
    public void onMessage(MessageExt messageExt) {
        String messageBody = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        OrderDomain orderDomain = JSON.parseObject(messageBody, OrderDomain.class);

        // create event
        DeductStockEvent deductStockEvent = new DeductStockEvent(this, orderDomain);
        // publish event
        applicationContext.publishEvent(deductStockEvent);

        /*
        // ※ keep idempotent: use promotion log, if already exists, return directly
        PromotionStockLogDomain promotionStockLog = promotionStockLogService.getPromotionLogByOrderIdAndOperation(orderDomain.getOrderNumber(), OperationName.DEDUCT_STOCK.toString());
        if (Objects.nonNull(promotionStockLog)) {
            return;
        }

        // deduct stock to database
        promotionService.deductStock(orderDomain.getPromotionId());

        // add promotion log
        promotionStockLogService.createPromotionLog(toPromotionStockLogDomain(orderDomain));

         */
    }

    /*
    private PromotionStockLogDomain toPromotionStockLogDomain(OrderDomain orderDomain) {
        return PromotionStockLogDomain.builder()
                .createTime(LocalDateTime.now())
                .promotionId(orderDomain.getPromotionId())
                .orderNumber(orderDomain.getOrderNumber())
                .operationName(OperationName.DEDUCT_STOCK)
                .userId(orderDomain.getUserId())
                .build();
    }

     */

}

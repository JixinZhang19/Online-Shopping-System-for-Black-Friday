package com.skillup.application.promotion.consumer;

import com.alibaba.fastjson2.JSON;
import com.skillup.domain.order.OrderDomain;
import com.skillup.domain.promotion.PromotionService;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@RocketMQMessageListener(topic = "${promotion.topic.lock-stock}", consumerGroup = "${promotion.topic.lock-stock-group}")
public class LockStockConsumer implements RocketMQListener<MessageExt> {

    @Autowired
    PromotionService promotionService;

    @Override
    public void onMessage(MessageExt messageExt) {
        String messageBody = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        OrderDomain orderDomain = JSON.parseObject(messageBody, OrderDomain.class);

        // lock stock to database
        promotionService.lockStock(orderDomain.getPromotionId());
    }
}


package com.skillup.application.order.consumer;

import com.alibaba.fastjson2.JSON;
import com.skillup.application.order.MqSendRepository;
import com.skillup.domain.order.OrderDomain;
import com.skillup.domain.order.OrderService;
import com.skillup.domain.order.util.OrderStatus;
import com.skillup.domain.stockCache.StockCacheDomain;
import com.skillup.domain.stockCache.StockCacheService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Objects;


@Slf4j
@Component
@RocketMQMessageListener(topic = "${order.topic.pay-check}", consumerGroup = "${order.topic.pay-check}")
public class PayCheckConsumer implements RocketMQListener<MessageExt> {

    @Autowired
    OrderService orderService;

    @Autowired
    StockCacheService stockCacheService;

    @Autowired
    MqSendRepository mqSendRepository;

    @Value("${promotion.topic.revert-stock}")
    String revertStockTopic;

    @Override
    public void onMessage(MessageExt messageExt) {
        String messageBody = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        OrderDomain orderDomain = JSON.parseObject(messageBody, OrderDomain.class);

        // 1. get newest orderDomain from database
        OrderDomain currentOrderDomain = orderService.getOrderById(orderDomain.getOrderNumber());
        if (Objects.isNull(currentOrderDomain)) {
            throw new RuntimeException("Order does not exist");
        }

        // 2. if unpaid: order status = 1 (created)
        if (currentOrderDomain.getOrderStatus().equals(OrderStatus.CREATED)) {
            // 2.1 set order status = 3 (overtime)
            currentOrderDomain.setOrderStatus(OrderStatus.OVERTIME);
            orderService.updateOrder(currentOrderDomain);
            // 2.2 revert stock in redis
            StockCacheDomain stockCacheDomain = StockCacheDomain.builder().promotionId(currentOrderDomain.getPromotionId()).build();
            stockCacheService.revertStock(stockCacheDomain);
            // 2.3 Send revert-stock to MySQL message to MQ
            mqSendRepository.sendMessageToTopic(revertStockTopic, JSON.toJSONString(orderDomain));
        }
        // 3. if paid: order status = 2 (paid)
        // meaning that deduct stock in database already executed after payment
        else if (currentOrderDomain.getOrderStatus().equals(OrderStatus.PAYED)) {
            log.info("OrderId: "+ currentOrderDomain.getOrderNumber() + " is already paid");
        }
        // 4. if already overtime/canceled
        else if (currentOrderDomain.getOrderStatus().equals(OrderStatus.OVERTIME)) {
            log.info("OrderId: "+ currentOrderDomain.getOrderNumber() + " is already overtime/canceled");
        }
    }
}

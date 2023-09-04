package com.skillup.application.order.consumer;

import com.alibaba.fastjson2.JSON;
import com.skillup.application.order.MqSendRepository;
import com.skillup.domain.order.OrderDomain;
import com.skillup.domain.order.OrderService;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@RocketMQMessageListener(topic = "${order.topic.create-order}", consumerGroup = "${order.topic.create-order-group}")
public class CreateOrderConsumer implements RocketMQListener<MessageExt> {

    @Autowired
    OrderService orderService;

    @Autowired
    MqSendRepository mqSendRepository;

    @Value("${promotion.topic.lock-stock}")
    String lockStockTopic;

    @Value("${order.topic.pay-check}")
    String payCheckTopic;

    @Override
    public void onMessage(MessageExt messageExt) {
        String messageBody = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        OrderDomain orderDomain = JSON.parseObject(messageBody, OrderDomain.class);

        // 1. Call database to create order
        orderService.createOrder(orderDomain);

        // 2. Send lock-stock to MySQL message to MQ
        mqSendRepository.sendMessageToTopic(lockStockTopic, JSON.toJSONString(orderDomain));

        // 3. Send pay-check message to MQ
        mqSendRepository.sendDelayMessageToTopic(payCheckTopic, JSON.toJSONString(orderDomain));
    }
}

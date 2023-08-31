package com.skillup.application.order.consumer;

import com.alibaba.fastjson2.JSON;
import com.skillup.domain.order.OrderDomain;
import com.skillup.domain.order.OrderService;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@RocketMQMessageListener(topic = "${order.topic.create-order}", consumerGroup = "${order.topic.create-order-group}")
public class CreateOrderConsumer implements RocketMQListener<MessageExt> {

    @Autowired
    OrderService orderService;

    @Override
    public void onMessage(MessageExt messageExt) {
        String messageBody = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        OrderDomain orderDomain = JSON.parseObject(messageBody, OrderDomain.class);
        // 4. call database to create order
        orderService.createOrder(orderDomain);

        // TODO: save lock stock info to database
    }
}

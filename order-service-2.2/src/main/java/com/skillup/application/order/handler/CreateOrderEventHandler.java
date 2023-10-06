package com.skillup.application.order.handler;

import com.alibaba.fastjson2.JSON;
import com.skillup.application.order.MqSendRepository;
import com.skillup.application.order.event.CreateOrderEvent;
import com.skillup.domain.order.OrderDomain;
import com.skillup.domain.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Component
public class CreateOrderEventHandler implements ApplicationListener<CreateOrderEvent> {

    @Autowired
    OrderService orderService;

    @Autowired
    MqSendRepository mqSendRepository;

    @Value("${promotion.topic.lock-stock}")
    String lockStockTopic;

    @Value("${order.topic.pay-check}")
    String payCheckTopic;

    @Override
    public void onApplicationEvent(CreateOrderEvent event) {

        OrderDomain orderDomain = event.orderDomain;

        // 1. Call database to create order
        // ※ keep idempotent: if already exists, return directly
        OrderDomain savedOrderDomain = orderService.getOrderById(orderDomain.getOrderNumber());
        if (Objects.nonNull(savedOrderDomain)) {
            return;
        }
        orderService.createOrder(orderDomain);

        // 2. Send lock-stock to MySQL message to MQ
        mqSendRepository.sendMessageToTopic(lockStockTopic, JSON.toJSONString(orderDomain));

        // 3. Send pay-check message to MQ
        mqSendRepository.sendDelayMessageToTopic(payCheckTopic, JSON.toJSONString(orderDomain));
    }
}

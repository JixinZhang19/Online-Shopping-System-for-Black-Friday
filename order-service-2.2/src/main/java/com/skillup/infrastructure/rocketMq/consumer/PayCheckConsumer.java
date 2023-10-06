package com.skillup.infrastructure.rocketMq.consumer;

import com.alibaba.fastjson2.JSON;
import com.skillup.application.order.event.PayCheckEvent;
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
@RocketMQMessageListener(topic = "${order.topic.pay-check}", consumerGroup = "${order.topic.pay-check-group}")
public class PayCheckConsumer implements RocketMQListener<MessageExt> {

    @Autowired
    ApplicationContext applicationContext;

    @Transactional
    @Override
    public void onMessage(MessageExt messageExt) {
        String messageBody = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        OrderDomain orderDomain = JSON.parseObject(messageBody, OrderDomain.class);

        // create event
        PayCheckEvent payCheckEvent = new PayCheckEvent(this, orderDomain);
        // publish event
        applicationContext.publishEvent(payCheckEvent);

    }
}

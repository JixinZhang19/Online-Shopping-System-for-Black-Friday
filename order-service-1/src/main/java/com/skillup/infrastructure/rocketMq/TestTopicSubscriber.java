package com.skillup.infrastructure.rocketMq;

import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@RocketMQMessageListener(topic = "test-topic", consumerGroup = "test-group")
public class TestTopicSubscriber implements RocketMQListener<MessageExt> {

    @Override
    public void onMessage(MessageExt messageExt) {
        System.out.println("===================== received ======================");
        String messageBody = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        System.out.println(messageBody);
    }
}

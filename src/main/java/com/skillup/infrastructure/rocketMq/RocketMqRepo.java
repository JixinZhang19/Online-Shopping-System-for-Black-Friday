package com.skillup.infrastructure.rocketMq;

import com.skillup.application.order.MqSendRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.nio.charset.StandardCharsets;

@Repository
@Slf4j
public class RocketMqRepo implements MqSendRepository {
    @Autowired
    RocketMQTemplate rocketMQTemplate;

    @Override
    public void sendMessageToTopic(String topic, String originMessage) {
        // 1. create rocketmq message
        Message message = new Message(topic, originMessage.getBytes(StandardCharsets.UTF_8));
        // 2. send rocketmq message to related topic
        try {
            rocketMQTemplate.getProducer().send(message);
            log.info("-- send message to rocketMQ --");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendDelayMessageToTopic(String topic, String originMessage) {

    }
}

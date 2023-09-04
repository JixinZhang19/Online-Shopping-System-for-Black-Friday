package com.skillup.infrastructure.rocketMq;

import com.skillup.application.order.MqSendRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.nio.charset.StandardCharsets;

@Repository
@Slf4j
public class RocketMqRepo implements MqSendRepository {
    @Autowired
    RocketMQTemplate rocketMQTemplate;

    @Value("${order.delay-time}")
    Integer delaySeconds;

    @Override
    public void sendMessageToTopic(String topic, String originMessage) {
        // 1. create rocketmq message
        Message message = new Message(topic, originMessage.getBytes(StandardCharsets.UTF_8));
        // 2. send rocketmq message to related topic
        try {
            rocketMQTemplate.getProducer().send(message);
            log.info("-- send a message " + topic + " to rocketMQ --");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendDelayMessageToTopic(String topic, String originMessage) {
        // 1. create rocketmq message
        Message message = new Message(topic, originMessage.getBytes(StandardCharsets.UTF_8));
        message.setDelayTimeLevel(secondsToRocketMqLevel(delaySeconds));
        // 2. send rocketmq delay message to related topic
        try {
            rocketMQTemplate.getProducer().send(message);
            log.info("-- send a delay message " + topic + " to rocketMQ --");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Integer secondsToRocketMqLevel(Integer seconds) {
        // 1-1s 2-5s 3-10s 4-30s 5-1m 6-2m 7-3m 8-4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
        if (seconds <= 1) return 1;
        if (seconds <= 5) return 2;
        if (seconds <= 10) return 3;
        if (seconds <= 30) return 4;
        if (seconds <= 60) return 5;
        else return 6;
    }
}

package com.skillup.infrastructure.rocketMq;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RocketMqRepoTest {
    @Autowired
    RocketMqRepo rocketMqRepo;

    @Test
    public void sendMessageToTopicTest() {
        rocketMqRepo.sendMessageToTopic("test-topic", "Hello, RocketMQ");
    }
}
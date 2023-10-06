package com.skillup.application.order;

public interface MqSendRepository {
    public void sendMessageToTopic(String topic, String originMessage);

    public void sendDelayMessageToTopic(String topic, String originMessage);
}

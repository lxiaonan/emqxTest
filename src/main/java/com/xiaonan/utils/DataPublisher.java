package com.xiaonan.utils;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
public class DataPublisher {

    private static final String BROKER = "tcp://localhost:1883";
    private static final String TOPIC = "test/topic";
    private static final String USERNAME = "test";
    private static final String PASSWORD = "test";
    private static final int MESSAGE_COUNT = 100;
    private static final Random RANDOM = new Random();

    public static void main(String[] args) throws MqttException {
        sendMessage();
    }

    public static void sendMessage() throws MqttException {
        try (MqttClient client = new MqttClient(BROKER, MqttClient.generateClientId())) {
            // MQTT 连接选项
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setUserName(USERNAME);
            connOpts.setPassword(PASSWORD.toCharArray());
            // 保留会话
            connOpts.setCleanSession(true);
            // 建立连接
            log.info("Connecting to broker: " + BROKER);
            client.connect(connOpts);
            // 循环发送消息
            for (int i = 0; i < MESSAGE_COUNT; i++) {
                // 获取当前时间的时间戳（秒）
                long currentTimeInSeconds = System.currentTimeMillis() / 1000;
                // 生成一个范围在 -30 到 +30 秒之间的随机数
                int randomOffset = RANDOM.nextInt(61) - 30; // [0, 60] -> [-30, 30]
                long timestamp = currentTimeInSeconds + randomOffset;
                String type = "ABCD".charAt(RANDOM.nextInt(4)) + "";
                String message = String.format("{\"timestamp\":%d, \"type\":\"%s\"}", timestamp, type);
                MqttMessage mqttMessage = new MqttMessage(message.getBytes(StandardCharsets.UTF_8));
                mqttMessage.setQos(1);
                client.publish(TOPIC, mqttMessage);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            log.info("发送成功");
            client.disconnect();
        }
    }
}

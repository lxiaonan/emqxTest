package com.xiaonan.controller;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/messages")
@Slf4j
public class MqttSubscriber {
    private static final String BROKER = "tcp://localhost:1883";
    private static final String TOPIC = "test/topic";
    private static final String USERNAME = "test";
    private static final String PASSWORD = "test";


    private final Map<Character, List<Long>> typeMap = new ConcurrentHashMap<>();

    public MqttSubscriber() {
        for (int i = 0; i < 4; i++) {
            typeMap.put((char) ('A' + i), new ArrayList<Long>());
        }
        try {
            MqttClient client = new MqttClient(BROKER, MqttClient.generateClientId());
            // MQTT 连接选项
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setUserName(USERNAME);
            connOpts.setPassword(PASSWORD.toCharArray());
            // 保留会话
            connOpts.setCleanSession(true);
            // 建立连接
            log.info("Connecting to broker: " + BROKER);
            client.connect(connOpts);
            // 订阅消息
            client.subscribe(TOPIC, (topic, message) -> {
                String payload = new String(message.getPayload());
                char type = extractType(payload);
                long time = extractTime(payload);
                typeMap.get(type).add(time);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回入参时间单位内的 A B C D 条数分别为多少
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 返回map集合
     */
    @GetMapping("/count")
    public Map<Character, Integer> getCount(@RequestParam long startTime, @RequestParam long endTime) {
        // 在此进行时间范围过滤并返回统计结果
        Map<Character, Integer> ans = new HashMap<>();
        Set<Character> characters = typeMap.keySet();
        for (char type : characters) {
            List<Long> times = typeMap.get(type);
            int sum = 0;
            for (long time : times) {
                if (time >= startTime && time <= endTime) sum++;
            }
            ans.put(type, sum);
        }
        return ans;
    }

    // 提取type
    private char extractType(String payload) {
        // 从 JSON 中提取 type 字段的简单方法
        return payload.split("\"type\":")[1].split("\"")[1].charAt(0);
    }

    // 提前时间戳
    private long extractTime(String payload) {
        return Long.valueOf(payload.split(",")[0].substring(payload.indexOf(":") + 1));
    }
}

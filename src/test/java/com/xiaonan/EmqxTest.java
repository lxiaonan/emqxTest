package com.xiaonan;

import com.xiaonan.controller.MqttSubscriber;
import com.xiaonan.utils.DataPublisher;
import org.eclipse.paho.client.mqttv3.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * EasyExcel 测试
 *
 * @author <a href="https://github.com/luo-yunan">小楠</a>
 * @author 小楠
 */
@SpringBootTest
public class EmqxTest  {
    @Test
    public void setUp() throws MqttException {
        MqttSubscriber mqttSubscriber = new MqttSubscriber();
        DataPublisher.sendMessage();
        Map<Character, Integer> count = mqttSubscriber.getCount(0, 9726738739l);
        System.out.println("结果是==>>" + count);
    }
}

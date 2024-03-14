package com.leikooo.mallchat.transaction.service;

import com.leikooo.mallchat.transaction.annotation.SecureInvoke;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import javax.annotation.Resource;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/3/14
 * @description
 */
public class MqProducer {
    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @SecureInvoke
    public void sendSecureMsg(String topic, Object body, String key) {
        Message<Object> msg = MessageBuilder.withPayload(body).setHeader("KEYS", key).build();
        rocketMQTemplate.send(topic, msg);
    }

    public void sendMsg(String topic, Object body, String key) {
        Message<Object> msg = MessageBuilder.withPayload(body).setHeader("KEYS", key).build();
        rocketMQTemplate.send(topic, msg);
    }
}

package com.leikooo.mallchat.common.chat.controller;

import com.leikooo.mallchat.transaction.service.MqProducer;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/3/14
 * @description
 */
@RequestMapping("/rocketMQ")
@RestController()
public class RocketMQController {
    @Resource
    private MqProducer mqProducer;

    @GetMapping("/sendMsg")
    @Transactional
    public void sendMsg(String topic, String body, String key) {
        mqProducer.sendSecureMsg(topic, body, key);
    }
}

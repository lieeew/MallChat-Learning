package com.leikooo.mallchat.common.rocketMQ;


import com.leikooo.mallchat.transaction.service.MqProducer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/3/14
 * @description
 */
@SpringBootTest
public class rocketMQ {
    @Resource
    private MqProducer mqProducer;

    @Test
    @Transactional
    void test() {
        mqProducer.sendSecureMsg("test-topic", "test", "test");
        try {
            Thread.sleep(1000 * 10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

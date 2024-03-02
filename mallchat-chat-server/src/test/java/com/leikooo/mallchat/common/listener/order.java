package com.leikooo.mallchat.common.listener;

import com.leikooo.mallchat.common.common.event.UserBlackEvent;
import com.leikooo.mallchat.common.user.domain.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;

import javax.annotation.Resource;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/3/2
 * @description
 */
@SpringBootTest
public class order {
    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @Test
    public void test() throws InterruptedException {
        applicationEventPublisher.publishEvent(new UserBlackEvent(this, new User()));
        Thread.sleep(1000000);
    }
}

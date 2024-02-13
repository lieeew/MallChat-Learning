package com.leikooo.mallchat.common.common.config;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;


/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/2/13
 * @description
 */
@SpringBootTest
@Slf4j
public class ThreadPoolConfigTest {
    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @SneakyThrows
    @Test
    public void mallchatExecutor() {
        threadPoolTaskExecutor.execute(() -> {
            log.error("test123456");
            throw new RuntimeException("test");
        });
    }
}
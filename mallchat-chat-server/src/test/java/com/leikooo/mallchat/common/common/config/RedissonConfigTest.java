package com.leikooo.mallchat.common.common.config;

import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/2/12
 * @description
 */
@SpringBootTest
public class RedissonConfigTest {
    @Resource
    private RedissonClient redissonClient;

    @Test
    public void redissonClient() {
        RLock lock = redissonClient.getLock("test");
        lock.lock();
        if (lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
        System.out.println("success");
    }
}
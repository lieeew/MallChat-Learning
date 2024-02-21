package com.leikooo.mallchat.common.common.service;

import com.leikooo.mallchat.common.common.exception.BusinessException;
import com.leikooo.mallchat.common.common.exception.CommonErrorEnum;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/2/19
 * @description
 */
@Slf4j
@Service
public class LockService {
    @Resource
    private RedissonClient redissonClient;

    @SneakyThrows
    public <T> T executeWithLock(String lockKey, int timeout, TimeUnit timeUnit, SupplierWithThrowable<T> supplier) {
        RLock lock = redissonClient.getLock(lockKey);
        boolean isSuccess = lock.tryLock(timeout, timeUnit);
        if (!isSuccess) {
            throw new BusinessException(CommonErrorEnum.LOCK_LIMIT);
        }
        try {
            return supplier.get();
        } finally {
            lock.unlock();
        }
    }


    @SneakyThrows
    public <T> T executeWithLock(String lockKey, Supplier<T> supplier) {
        RLock lock = redissonClient.getLock(lockKey);
        boolean isSuccess = lock.tryLock(-1, TimeUnit.MICROSECONDS);
        if (!isSuccess) {
            throw new BusinessException(CommonErrorEnum.LOCK_LIMIT);
        }
        try {
            return supplier.get();
        } finally {
            lock.unlock();
        }
    }

    @SneakyThrows
    public void executeWithLock(String lockKey, Runnable runnable) {
        RLock lock = redissonClient.getLock(lockKey);
        boolean isSuccess = lock.tryLock(-1, TimeUnit.MICROSECONDS);
        if (!isSuccess) {
            throw new BusinessException(CommonErrorEnum.LOCK_LIMIT);
        }
        try {
            runnable.run();
        } finally {
            lock.unlock();
        }
    }
}



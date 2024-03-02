package com.leikooo.mallchat.common.common.aspect;

import com.leikooo.mallchat.common.common.annotation.RedissonLock;
import com.leikooo.mallchat.common.common.service.LockService;
import com.leikooo.mallchat.common.common.service.SupplierWithThrowable;
import com.leikooo.mallchat.common.common.utils.SpElUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/2/21
 * @description
 * @see SupplierWithThrowable
 */
@Aspect
@Order(0) // 这里的 order 优先级必须比事务的优先级高，如果事务的优先级高，会导致事务失效
@Component
public class RedissonLockAspect {
    @Resource
    private LockService lockService;

    @Around("@annotation(redissonLock)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint, RedissonLock redissonLock) throws Throwable {
        Method method = ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod();
        String prefixKey = StringUtils.isEmpty(redissonLock.prefix()) ? SpElUtils.getMethodKey(method) : redissonLock.prefix();
        String key = SpElUtils.parseSpEl(method, proceedingJoinPoint.getArgs(), redissonLock.key());
        return lockService.executeWithLock(prefixKey + key, redissonLock.waitTime(), redissonLock.timeUnit(), new SupplierWithThrowable<Object>() {
            @Override
            public Object get() throws Throwable {
                return proceedingJoinPoint.proceed();
            }
        });
    }
}

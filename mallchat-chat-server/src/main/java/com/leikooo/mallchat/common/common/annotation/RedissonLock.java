package com.leikooo.mallchat.common.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/2/20
 * @description
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RedissonLock {
    /**
     * key 的前缀
     */
    String prefix() default "";

    /**
     * key 支持 SpEL 表达式 例如 #userId
     */
    String key();

    /**
     * 等待时间，默认不等待
     */
    int waitTime() default -1;

    /**
     * 单位默认 ms
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;
}

package com.leikooo.mallchat.common.common.thread;

import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/2/13
 * @description
 */
@Slf4j
public class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.error("Exception in thread " + t.getName(), e);
    }
}

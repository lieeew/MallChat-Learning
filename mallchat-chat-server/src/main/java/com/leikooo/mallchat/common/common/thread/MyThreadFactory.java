package com.leikooo.mallchat.common.common.thread;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ThreadFactory;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/2/13
 * @description 装饰器模式
 */
@AllArgsConstructor
public class MyThreadFactory implements ThreadFactory {
    private static final MyUncaughtExceptionHandler MY_UNCAUGHT_EXCEPTION_HANDLER = new MyUncaughtExceptionHandler();

    private ThreadFactory threadFactory;

    /**
     * 装饰器模式
     *
     * @param r
     * @return
     */
    @Override
    public Thread newThread(Runnable r) {
        Thread thread = threadFactory.newThread(r);
        thread.setUncaughtExceptionHandler(MY_UNCAUGHT_EXCEPTION_HANDLER);
        return thread;
    }
}

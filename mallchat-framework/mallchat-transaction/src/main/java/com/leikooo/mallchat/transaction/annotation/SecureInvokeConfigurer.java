package com.leikooo.mallchat.transaction.annotation;

import io.micrometer.core.lang.Nullable;

import java.util.concurrent.Executor;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 */
public interface SecureInvokeConfigurer {

    /**
     * 返回一个线程池
     */
    @Nullable
    default Executor getSecureInvokeExecutor() {
        return null;
    }

}

package com.leikooo.frequency.util;


import com.leikooo.frequency.domain.dto.RequestInfo;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/2/16
 * @description 利用 ThreadLocal 存储当前请求的用户信息
 */
public class RequestHolder {
    private static final ThreadLocal<RequestInfo> THREAD_LOCAL = new ThreadLocal<>();

    public static void set(RequestInfo requestInfo) {
        THREAD_LOCAL.set(requestInfo);
    }

    public static RequestInfo get() {
        return THREAD_LOCAL.get();
    }

    /**
     * 一定要记得清除，否则会造成内存泄漏
     */
    public static void remove() {
        THREAD_LOCAL.remove();
    }
}

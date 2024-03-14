package com.leikooo.mallchat.transaction.service;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/3/14
 * @description
 */
public class SecureInvokeHolder {
    public static final ThreadLocal<Boolean> IN_TRANSACTION = new ThreadLocal<>();

    public static Boolean getIsInTransaction() {
        return IN_TRANSACTION.get();
    }

    public static void setInTransaction() {
        IN_TRANSACTION.set(true);
    }

    public static void removeInTransaction() {
        IN_TRANSACTION.remove();
    }
}

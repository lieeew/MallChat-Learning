package com.leikooo.mallchat.common.transaction;

import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/3/17
 * @description 测试 @Transactional 注解
 */
@Component
public class TransactionalTest {
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void test() {
        // 这个可以获取事务的详细信息
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            String currentTransactionName = TransactionSynchronizationManager.getCurrentTransactionName();
            TransactionStatus transactionStatus = TransactionAspectSupport.currentTransactionStatus();
        }
        System.out.println("TransactionalTest");
    }

    @Transactional
    public void test2() {
        // 还是在对应的 test 的事务之中
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            String currentTransactionName = TransactionSynchronizationManager.getCurrentTransactionName();
            TransactionStatus transactionStatus = TransactionAspectSupport.currentTransactionStatus();
        }
        test();
    }
}

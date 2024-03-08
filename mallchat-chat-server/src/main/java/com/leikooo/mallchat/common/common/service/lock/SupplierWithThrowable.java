package com.leikooo.mallchat.common.common.service.lock;

import com.leikooo.mallchat.common.common.aspect.RedissonLockAspect;

/**
 * 这个接口没什么，但是想要传一个这个类型的接口，就需要实现这个接口的所有方法，当然也就是匿名实现类
 * 就比如困扰我很久的
 * @see RedissonLockAspect
 * 这个类里面的 executeWithLock 方法，就是传入了一个 SupplierWithThrowable 接口的匿名实现类
 * 完整的写法是这样，但是有简单的写法
 *    return lockService.executeWithLock(。。。。, new SupplierWithThrowable<T>() {
 *        @Override
 *        public Object get() throws Throwable {
 *            return proceedingJoinPoint.proceed();
 *        }
 *    });
 *    简化后的代码
 *    return lockService.executeWithLock(。。。, proceedingJoinPoint.proceed());
 *   总结：
 *  所以在 LockService 里面调用 supplier.get() 方法的时候，就会调用这个匿名实现类的 get 方法
 *  所以才会继续执行到标识注解的方法内部，之前还一直以为是 spring 框架整的，这个 @FunctionalInterface 只是声明函数似接口
 *  还是 java 基础不扎实，老韩说的没错啊，学到后面还是 Java 基础
 *
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/2/21
 */
@FunctionalInterface
public interface SupplierWithThrowable<T> {

    /**
     * Gets a result.
     *
     * @return a result
     */
    T get() throws Throwable;
}
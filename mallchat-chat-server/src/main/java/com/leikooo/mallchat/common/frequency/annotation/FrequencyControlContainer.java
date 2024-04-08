package com.leikooo.mallchat.common.frequency.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author leikooo
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)// 运行时生效
public @interface FrequencyControlContainer {
    FrequencyControl[] value();
}

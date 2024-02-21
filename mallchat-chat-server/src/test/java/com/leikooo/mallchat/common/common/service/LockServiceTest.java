package com.leikooo.mallchat.common.common.service;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Parameter;

public class LockServiceTest {
    public void test1(String aaa, int xxx) {

    }

    @Test
    void test() throws NoSuchMethodException {
        Parameter[] parameters = LockServiceTest.class.getMethod("test1", String.class, int.class).getParameters();
        for (Parameter parameter : parameters) {
            System.out.println(parameter.getName());
        }
    }
}
package com.leikooo.mallchat.common.util;

import com.leikooo.mallchat.common.common.utils.RedisUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/2/4
 * @description
 */
@SpringBootTest
public class RedisUtilsTest {

    @Test
    void test() {
        RedisUtils.set("leikooo", 111);
        System.out.println("RedisUtils.getStr(\"leikooo\") = " + RedisUtils.getStr("leikooo"));
    }

}
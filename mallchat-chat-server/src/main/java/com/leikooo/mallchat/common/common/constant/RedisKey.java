package com.leikooo.mallchat.common.common.constant;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/2/12
 * @description
 */
public class RedisKey {
    public static final String BASE_REDIS_KEY = "mallChat:chat";

    /**
     * 用户 token
     */
    public static final String USER_TOKEN = "user:token:uid_%s";

    public static String getKey(String redisKey, Object... args) {
        return BASE_REDIS_KEY + String.format(USER_TOKEN, args);
    }
}

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

    /**
     * 用户的信息汇总
     */
    public static final String USER_SUMMARY_STRING = "user:summary:uid_%d";

    /**
     * 用户的物品 key
     */
    public static final String USER_ITEM_STRING = "user:item:item_%d";

    /**
     * 用户的背包 key
     */
    public static final String USER_BACKPACK_STRING = "user:backpack:id_%d";

    /**
     * 用户 modify 时间
     */
    public static final String USER_MODIFY_STRING = "user:modify:uid_%d";

    /**
     * 用户信息
     */
    public static final String USER_INFO_STRING = "user:info:uid_%d";

    /**
     * 热门群聊 key
     */
    public static final String HOT_ROOM_ZSET = "hot:room:zset";

    public static String getKey(String redisKey, Object... args) {
        return BASE_REDIS_KEY + String.format(redisKey, args);
    }
}

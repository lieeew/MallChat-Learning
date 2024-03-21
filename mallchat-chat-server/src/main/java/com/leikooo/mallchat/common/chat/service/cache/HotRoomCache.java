package com.leikooo.mallchat.common.chat.service.cache;

import com.leikooo.mallchat.common.common.constant.RedisKey;
import com.leikooo.mallchat.common.common.utils.RedisUtils;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/3/19
 * @description
 */
@Component
public class HotRoomCache {
    public void refreshActiveTime(Long roomId, Date date) {
        RedisUtils.zAdd(RedisKey.getKey(RedisKey.HOT_ROOM_ZSET), roomId, date.getTime());
    }
}

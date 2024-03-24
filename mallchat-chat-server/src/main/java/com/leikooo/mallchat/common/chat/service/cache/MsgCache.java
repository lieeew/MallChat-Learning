package com.leikooo.mallchat.common.chat.service.cache;

import com.leikooo.mallchat.common.chat.dao.MessageDao;
import com.leikooo.mallchat.common.chat.domain.entity.Message;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author <a href="https://www.github.com/lieeew">leikooo</a>
 * @description 消息缓存
 */
@Component
public class MsgCache {
    @Resource
    private MessageDao messageDao;

    @Cacheable(cacheNames = "msg", key = "'msg' + #msgId")
    public Message getMsg(Long msgId) {
        return messageDao.getById(msgId);
    }

    @CacheEvict(cacheNames = "msg", key = "'msg' + #msgId")
    public void evictMsg(Long msgId) {
    }
}

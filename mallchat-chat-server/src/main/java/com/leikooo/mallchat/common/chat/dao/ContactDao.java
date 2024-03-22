package com.leikooo.mallchat.common.chat.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leikooo.mallchat.common.chat.domain.entity.Contact;
import com.leikooo.mallchat.common.chat.mapper.ContactMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 会话列表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @since 2024-03-02
 */
@Service
public class ContactDao extends ServiceImpl<ContactMapper, Contact> {

    public void refreshOrCreateActiveTime(Long roomId, List<Long> memberUidList, Long msgId, Date activeTime) {
        baseMapper.refreshOrCreateActiveTime(roomId, memberUidList, msgId, activeTime);
    }

    public Contact getLastMessageId(Long roomId, Long uid) {
        return lambdaQuery()
                .eq(Contact::getRoomId, roomId)
                .eq(Contact::getUid, uid)
                .one();
    }
}

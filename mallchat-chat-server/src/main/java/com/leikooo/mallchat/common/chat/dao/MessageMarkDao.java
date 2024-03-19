package com.leikooo.mallchat.common.chat.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leikooo.mallchat.common.chat.domain.entity.Message;
import com.leikooo.mallchat.common.chat.domain.entity.MessageMark;
import com.leikooo.mallchat.common.chat.mapper.MessageMarkMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 消息标记表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @since 2024-03-18
 */
@Service
public class MessageMarkDao extends ServiceImpl<MessageMarkMapper, MessageMark> {

    public List<MessageMark> getValidMessageMark(Long receiveUid, List<Message> messages) {
        return lambdaQuery()
                .in(MessageMark::getMsgId, messages.stream().map(Message::getId).toArray())
                .eq(MessageMark::getUid, receiveUid)
                .list();
    }
}

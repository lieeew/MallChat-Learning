package com.leikooo.mallchat.common.chat.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leikooo.mallchat.common.chat.domain.entity.Message;
import com.leikooo.mallchat.common.chat.domain.enums.MessageStatusEnum;
import com.leikooo.mallchat.common.chat.domain.vo.request.ChatMessagePageReq;
import com.leikooo.mallchat.common.chat.mapper.MessageMapper;
import com.leikooo.mallchat.common.common.domain.vo.response.CursorPageBaseResp;
import com.leikooo.mallchat.common.common.utils.CursorUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * <p>
 * 消息表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @since 2024-03-02
 */
@Service
public class MessageDao extends ServiceImpl<MessageMapper, Message> {

    public CursorPageBaseResp<Message> getCursorPage(ChatMessagePageReq req, Long lastMessageId) {
        return CursorUtils.getCursorPageByMysql(this, req, wrapper -> {
            wrapper.eq(Message::getStatus, MessageStatusEnum.NORMAL.getStatus());
            wrapper.eq(Message::getRoomId, req.getRoomId());
            wrapper.le(Objects.nonNull(lastMessageId), Message::getId, lastMessageId);
        }, Message::getId);
    }

    public Message getRoomMessage(Long roomId, Long msgId) {
        return lambdaQuery()
                .eq(Message::getRoomId, roomId)
                .eq(Message::getId, msgId)
                .one();
    }

    public Integer getGapCount(Long roomId, Long fromId, Long toId) {
        return lambdaQuery()
                .eq(Message::getRoomId, roomId)
                .gt(Message::getId, fromId)
                .le(Message::getId, toId)
                .count();
    }
}

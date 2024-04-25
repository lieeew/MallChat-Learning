package com.leikooo.mallchat.common.common.event.listenser;

import com.leikooo.mallchat.common.chat.dao.MessageMarkDao;
import com.leikooo.mallchat.common.chat.domain.dto.ChatMessageMarkDTO;
import com.leikooo.mallchat.common.chat.domain.entity.Message;
import com.leikooo.mallchat.common.chat.domain.enums.MessageMarkTypeEnum;
import com.leikooo.mallchat.common.chat.domain.enums.MessageTypeEnum;
import com.leikooo.mallchat.common.chat.service.cache.MsgCache;
import com.leikooo.mallchat.common.common.domain.enums.IdempotentEnum;
import com.leikooo.mallchat.common.common.event.MessageMarkEvent;
import com.leikooo.mallchat.common.user.domain.enums.ItemEnum;
import com.leikooo.mallchat.common.user.service.UserBackpackService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/2/22
 * @description
 */
@Component
public class MessageMarkListener {
    @Resource
    private MsgCache msgCache;

    @Resource
    private MessageMarkDao messageMarkDao;

    @Resource
    private UserBackpackService userBackpackService;

    @EventListener(MessageMarkEvent.class)
    public void consumerMessage(MessageMarkEvent messageMarkEvent) {
        ChatMessageMarkDTO messageMarkDto = messageMarkEvent.getChatMessageMarkDTO();
        Message message = msgCache.getMsg(messageMarkDto.getMsgId());
        if (!Objects.equals(message.getType(), MessageTypeEnum.TEXT.getType())) {
            return;
        }
        if (checkIfReachRiseNum(messageMarkDto)) {
            userBackpackService.acquireItem(message.getFromUid(), ItemEnum.LIKE_BADGE.getId(), IdempotentEnum.UID.getType(), message.getFromUid().toString());
        }
    }

    private boolean checkIfReachRiseNum(ChatMessageMarkDTO messageMarkDto) {
        Integer markCount = messageMarkDao.getMarkCount(messageMarkDto.getMsgId(), messageMarkDto.getMarkType());
        MessageMarkTypeEnum markTypeEnum = MessageMarkTypeEnum.of(messageMarkDto.getMarkType());
        if (markCount < markTypeEnum.getRiseNum()) {
             return false;
        }
        return Objects.equals(MessageMarkTypeEnum.LIKE.getType(), messageMarkDto.getMarkType());
    }
}

package com.leikooo.mallchat.common.common.event.listenser;

import com.leikooo.mallchat.common.chat.dao.MessageMarkDao;
import com.leikooo.mallchat.common.chat.domain.entity.MessageMark;
import com.leikooo.mallchat.common.common.constant.MQConstant;
import com.leikooo.mallchat.common.common.domain.dto.MsgSendMessageDTO;
import com.leikooo.mallchat.common.common.event.MessageSendEvent;
import com.leikooo.mallchat.transaction.service.MqProducer;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.annotation.Resource;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/2/22
 * @description
 */
@Component
public class MessageSendListener {
    @Resource
    private MqProducer mqProducer;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT, classes = MessageSendEvent.class)
    public void receiveBadge(MessageSendEvent event) {
        Long messageId = event.getMessageId();
        mqProducer.sendSecureMsg(MQConstant.SEND_MSG_TOPIC, new MsgSendMessageDTO(messageId), messageId);
    }
}

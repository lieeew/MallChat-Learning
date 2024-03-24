package com.leikooo.mallchat.common.common.event.listenser;

import com.leikooo.mallchat.common.chat.domain.dto.ChatMsgRecallDTO;
import com.leikooo.mallchat.common.chat.domain.entity.Message;
import com.leikooo.mallchat.common.chat.service.cache.MsgCache;
import com.leikooo.mallchat.common.common.constant.MQConstant;
import com.leikooo.mallchat.common.common.domain.dto.MsgSendMessageDTO;
import com.leikooo.mallchat.common.common.event.MessageRecallEvent;
import com.leikooo.mallchat.common.common.event.MessageSendEvent;
import com.leikooo.mallchat.common.user.adapter.WebSocketAdapter;
import com.leikooo.mallchat.common.user.dao.UserDao;
import com.leikooo.mallchat.common.user.domain.entity.User;
import com.leikooo.mallchat.common.user.service.WebSocketService;
import com.leikooo.mallchat.common.user.service.cache.UserCache;
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
public class MessageRecallListener {
    @Resource
    private MsgCache msgCache;

    @Resource
    private WebSocketService webSocketService;

    @Resource
    private UserDao userDao;

    @EventListener(classes = MessageRecallEvent.class)
    public void evictMessageCache(MessageRecallEvent messageRecallEvent) {
        ChatMsgRecallDTO chatMsgRecallDTO = messageRecallEvent.getChatMsgRecallDTO();
        Long msgId = chatMsgRecallDTO.getMsgId();
        msgCache.evictMsg(msgId);
    }
    @EventListener(classes = MessageRecallEvent.class)
    public void sendRecallMsg(MessageRecallEvent messageRecallEvent) {
        ChatMsgRecallDTO chatMsgRecallDTO = messageRecallEvent.getChatMsgRecallDTO();
        Long recallUid = chatMsgRecallDTO.getRecallUid();
        Message msg = msgCache.getMsg(chatMsgRecallDTO.getMsgId());
        User user = userDao.getById(recallUid);
        webSocketService.sendToAllOnline(WebSocketAdapter.buildRecallMessage(msg, user));
    }

}

package com.leikooo.mallchat.common.chat.consumer;

import com.leikooo.mallchat.common.chat.dao.MessageDao;
import com.leikooo.mallchat.common.chat.dao.RoomDao;
import com.leikooo.mallchat.common.chat.domain.entity.Message;
import com.leikooo.mallchat.common.chat.domain.entity.Room;
import com.leikooo.mallchat.common.chat.domain.vo.response.ChatMessageResp;
import com.leikooo.mallchat.common.chat.service.ChatService;
import com.leikooo.mallchat.common.common.constant.MQConstant;
import com.leikooo.mallchat.common.common.domain.dto.MsgSendMessageDTO;
import com.leikooo.mallchat.common.common.utils.RedisUtils;
import com.leikooo.mallchat.common.user.adapter.WebSocketAdapter;
import com.leikooo.mallchat.common.user.service.WebSocketService;
import jodd.exception.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/3/15
 * @description
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = MQConstant.SEND_MSG_TOPIC, consumerGroup = MQConstant.SEND_MSG_GROUP)
public class MessageSendConsumer implements RocketMQListener<MsgSendMessageDTO> {
    @Resource
    private MessageDao messageDao;

    @Resource
    private RoomDao roomDao;

    @Resource
    private WebSocketService webSocketService;

    @Resource
    private ChatService chatService;

    @Override
    public void onMessage(MsgSendMessageDTO message) {
        log.info("MessageSendConsumer 收到消息{}", message);

        Long msgId = message.getMsgId();
        Message msg = messageDao.getById(msgId);
        Room room = roomDao.getById(msg.getRoomId());
        try {
            chatService.getChatMessageResp(msg, msg.getFromUid());
        } catch (Exception e) {
            log.error("MessageSendConsumer getChatMessageResp error", e);
        }
//        if (room.isHotRoom()) {
//            ChatMessageResp chatMessageResp = new ChatMessageResp();
////            RedisUtils.zAdd()
//            webSocketService.sendToAllOnline(WebSocketAdapter.buildNewMsgResp(chatMessageResp));
//        }
//        if (room.isRoomGroup()) {
//
//        }
    }
}

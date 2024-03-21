package com.leikooo.mallchat.common.chat.consumer;

import com.leikooo.mallchat.common.chat.dao.ContactDao;
import com.leikooo.mallchat.common.chat.dao.MessageDao;
import com.leikooo.mallchat.common.chat.dao.RoomDao;
import com.leikooo.mallchat.common.chat.dao.RoomFriendDao;
import com.leikooo.mallchat.common.chat.domain.entity.Message;
import com.leikooo.mallchat.common.chat.domain.entity.Room;
import com.leikooo.mallchat.common.chat.domain.entity.RoomFriend;
import com.leikooo.mallchat.common.chat.domain.vo.response.ChatMessageResp;
import com.leikooo.mallchat.common.chat.service.ChatService;
import com.leikooo.mallchat.common.chat.service.cache.HotRoomCache;
import com.leikooo.mallchat.common.chat.service.cache.RoomCache;
import com.leikooo.mallchat.common.chat.service.impl.PushService;
import com.leikooo.mallchat.common.common.constant.MQConstant;
import com.leikooo.mallchat.common.common.domain.dto.MsgSendMessageDTO;
import com.leikooo.mallchat.common.common.domain.dto.PushMessageDTO;
import com.leikooo.mallchat.common.common.utils.RedisUtils;
import com.leikooo.mallchat.common.user.adapter.WebSocketAdapter;
import com.leikooo.mallchat.common.user.domain.enums.WSPushTypeEnum;
import com.leikooo.mallchat.common.user.service.WebSocketService;
import com.mysql.cj.x.protobuf.MysqlxExpr;
import jodd.exception.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private PushService pushService;

    @Resource
    private ChatService chatService;

    @Resource
    private HotRoomCache hotRoomCache;

    @Resource
    private RoomCache roomCache;

    @Resource
    private RoomFriendDao roomFriendDao;

    @Resource
    private ContactDao contactDao;

    @Override
    public void onMessage(MsgSendMessageDTO message) {
        log.info("MessageSendConsumer 收到消息{}", message);
        Long msgId = message.getMsgId();
        Message msg = messageDao.getById(msgId);
        Room room = roomDao.getById(msg.getRoomId());
        // 刷新房间的时间 -redis
        roomDao.refreshActiveTime(msg.getRoomId(), msg.getCreateTime(), message.getMsgId());
        ChatMessageResp chatMessageResp = chatService.getChatMessageResp(msg, null);
        if (room.isHotRoom()) {
            // 存储到 redis 里面
            hotRoomCache.refreshActiveTime(msg.getRoomId(), msg.getCreateTime());
            pushService.pushMsg(WebSocketAdapter.buildPushAllMsg(chatMessageResp));
            return;
        }
        List<Long> memberList = new ArrayList<>();
        if (room.isRoomGroup()) {
            // 获取到房间的所有人员 uid
            memberList = roomCache.getRoomUsersId(room.getId());
        }
        if (room.isRoomFriend()) {
            // 推送给好友消息
            RoomFriend roomFriend = roomFriendDao.getRoomFriendByRoomId(room.getId());
            memberList = Arrays.asList(roomFriend.getUid2(), roomFriend.getUid1());
        }
        // 更新所有群成员的会话时间
        contactDao.refreshOrCreateActiveTime(room.getId(), memberList, msg.getId(), msg.getCreateTime());
        // 推送给房间的所有人员
        pushService.pushMsg(WebSocketAdapter.buildPushMsg(chatMessageResp, memberList));
    }
}

package com.leikooo.mallchat.common.chat.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.leikooo.mallchat.common.chat.dao.GroupMemberDao;
import com.leikooo.mallchat.common.chat.dao.MessageMarkDao;
import com.leikooo.mallchat.common.chat.dao.RoomDao;
import com.leikooo.mallchat.common.chat.dao.RoomFriendDao;
import com.leikooo.mallchat.common.chat.domain.entity.*;
import com.leikooo.mallchat.common.chat.domain.entity.msg.MessageExtra;
import com.leikooo.mallchat.common.chat.domain.enums.MessageTypeEnum;
import com.leikooo.mallchat.common.chat.domain.vo.request.ChatMessageReq;
import com.leikooo.mallchat.common.chat.domain.vo.response.ChatMessageResp;
import com.leikooo.mallchat.common.chat.service.ChatService;
import com.leikooo.mallchat.common.chat.service.adapter.MessageAdapter;
import com.leikooo.mallchat.common.chat.service.factory.MsgHandlerFactory;
import com.leikooo.mallchat.common.chat.service.strategy.msg.AbstractMsgHandler;
import com.leikooo.mallchat.common.common.domain.enums.YesOrNoEnum;
import com.leikooo.mallchat.common.common.event.MessageSendEvent;
import com.leikooo.mallchat.common.common.utils.AssertUtil;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.leikooo.mallchat.common.chat.service.factory.MsgHandlerFactory.msgHandlerMap;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/3/11
 * @description
 */
@Service
public class ChatServiceImpl implements ChatService {
    @Resource
    private RoomDao roomDao;

    @Resource
    private GroupMemberDao groupMemberDao;

    @Resource
    private RoomFriendDao roomFriendDao;

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @Resource
    private MessageMarkDao messageMarkDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendMsg(Long uid, ChatMessageReq req) {
        check(uid, req);
        // 这个调用方式会导致事务失效，对应的实例是不是 Spring 代理对象, 但是这样调用 checkAndSave 方法还是会在事务中执行
        // 而且我通过 new 对象创建对象调用对应的方法也是可以在事务中执行的，而且 this.这个类的方法也是在事务中执行的
        AbstractMsgHandler<?> msgHandler = MsgHandlerFactory.getStrategyNoNull(req.getMsgType());
        Long messageId = msgHandler.checkAndSave(req, uid);
        applicationEventPublisher.publishEvent(new MessageSendEvent(this, messageId));
    }

    @Override
    public ChatMessageResp getChatMessageResp(Message message, Long receiveUid) {
        return CollUtil.getFirst(getMsgRespBatch(Collections.singletonList(message), receiveUid));
    }

    @Override
    public List<ChatMessageResp> getMsgRespBatch(List<Message> messages, Long receiveUid) {
        if (CollectionUtils.isEmpty(messages)) {
            return new ArrayList<>();
        }
        List<MessageMark> validMessageMark = messageMarkDao.getValidMessageMark(receiveUid, messages);
        return MessageAdapter.buildMsgResp(validMessageMark, messages);
    }

    private void check(Long uid, ChatMessageReq req) {
        Room room = roomDao.getById(req.getRoomId());
        AssertUtil.isNotEmpty(room, "房间不存在");
        // 热点群聊
        if (room.isHotRoom()) {
            return;
        }
        if (room.isRoomGroup()) {
            GroupMember groupMember = groupMemberDao.getByGroupId(room.getId(), uid);
            AssertUtil.isNotEmpty(groupMember, "不是群成员");
            return;
        }
        if (room.isRoomFriend()) {
            RoomFriend roomFriend = roomFriendDao.getRoomFriendByRoomId(room.getId());
            AssertUtil.equal(roomFriend.getStatus(), YesOrNoEnum.NO.getStatus(), "房间已经被封禁");
            AssertUtil.isTrue(Objects.equals(uid, roomFriend.getUid1()) || Objects.equals(uid, roomFriend.getUid2()), "不是房间成员");
        }
    }
}

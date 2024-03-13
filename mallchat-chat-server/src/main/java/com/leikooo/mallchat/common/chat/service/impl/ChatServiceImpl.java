package com.leikooo.mallchat.common.chat.service.impl;

import com.leikooo.mallchat.common.chat.dao.GroupMemberDao;
import com.leikooo.mallchat.common.chat.dao.MessageDao;
import com.leikooo.mallchat.common.chat.dao.RoomDao;
import com.leikooo.mallchat.common.chat.dao.RoomFriendDao;
import com.leikooo.mallchat.common.chat.domain.entity.GroupMember;
import com.leikooo.mallchat.common.chat.domain.entity.Message;
import com.leikooo.mallchat.common.chat.domain.entity.Room;
import com.leikooo.mallchat.common.chat.domain.entity.RoomFriend;
import com.leikooo.mallchat.common.chat.domain.enums.MessageType;
import com.leikooo.mallchat.common.chat.domain.enums.MessageTypeEnum;
import com.leikooo.mallchat.common.chat.domain.vo.request.ChatMessageReq;
import com.leikooo.mallchat.common.chat.domain.vo.response.ChatMessageResp;
import com.leikooo.mallchat.common.chat.domain.vo.response.ChatRoomResp;
import com.leikooo.mallchat.common.chat.domain.vo.response.msg.TextMsgResp;
import com.leikooo.mallchat.common.chat.service.ChatService;
import com.leikooo.mallchat.common.chat.service.strategy.msg.AbstractMsgHandler;
import com.leikooo.mallchat.common.common.domain.enums.YesOrNoEnum;
import com.leikooo.mallchat.common.common.event.MessageSendEvent;
import com.leikooo.mallchat.common.common.utils.AssertUtil;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    @Override
    public void sendMsg(Long uid, ChatMessageReq req) {
        check(uid, req);
        AbstractMsgHandler<?> msgHandler = msgHandlerMap.get(req.getMsgType());
        Long messageId = msgHandler.checkAndSaveMsg(req, uid);
        applicationEventPublisher.publishEvent(new MessageSendEvent(this, messageId));
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

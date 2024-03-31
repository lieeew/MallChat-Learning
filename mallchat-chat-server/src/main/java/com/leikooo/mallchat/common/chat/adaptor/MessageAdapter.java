package com.leikooo.mallchat.common.chat.adaptor;

import com.leikooo.mallchat.common.chat.domain.entity.Message;
import com.leikooo.mallchat.common.chat.domain.entity.MessageMark;
import com.leikooo.mallchat.common.chat.domain.entity.msg.MessageExtra;
import com.leikooo.mallchat.common.chat.domain.entity.msg.MsgRecall;
import com.leikooo.mallchat.common.chat.domain.enums.MessageMarkTypeEnum;
import com.leikooo.mallchat.common.chat.domain.enums.MessageTypeEnum;
import com.leikooo.mallchat.common.chat.domain.vo.request.ChatMessageReq;
import com.leikooo.mallchat.common.chat.domain.vo.response.ChatMessageResp;
import com.leikooo.mallchat.common.chat.domain.vo.response.msg.TextMsgResp;
import com.leikooo.mallchat.common.chat.service.factory.MsgHandlerFactory;
import com.leikooo.mallchat.common.common.domain.enums.YesOrNoEnum;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/3/18
 * @description
 */
public class MessageAdapter {

    public static List<ChatMessageResp> buildMsgResp(List<MessageMark> messageMarks, List<Message> messages) {
        return messages.stream().filter(Objects::nonNull).map(message -> {
            ChatMessageResp chatMessageResp = new ChatMessageResp();
            chatMessageResp.setMessage(buildMessage(message, messageMarks, message.getFromUid()));
            chatMessageResp.setFromUser(buildFromUserInfo(message.getFromUid()));
            return chatMessageResp;
        }).collect(Collectors.toList());
    }

    private static ChatMessageResp.Message buildMessage(Message message, List<MessageMark> marks, Long receiveUid) {
        ChatMessageResp.Message messageVO = ChatMessageResp.Message.builder().id(message.getId()).roomId(message.getRoomId()).sendTime(message.getCreateTime()).type(message.getType()).build();
        messageVO.setBody((MsgHandlerFactory.getStrategyNoNull(message.getType())).showMsg(message));
        messageVO.setMessageMark(buildMessageMark(marks, receiveUid));
        return messageVO;
    }

    private static ChatMessageResp.UserInfo buildFromUserInfo(Long uid) {
        return ChatMessageResp.UserInfo.builder().uid(uid).build();
    }

    private static ChatMessageResp.MessageMark buildMessageMark(List<MessageMark> marks, Long receiveUid) {
        Map<Integer, List<MessageMark>> typeMap = marks.stream().collect(Collectors.groupingBy(MessageMark::getType));
        List<MessageMark> likeMarks = typeMap.getOrDefault(MessageMarkTypeEnum.LIKE.getType(), new ArrayList<>());
        List<MessageMark> dislikeMarks = typeMap.getOrDefault(MessageMarkTypeEnum.DISLIKE.getType(), new ArrayList<>());
        ChatMessageResp.MessageMark mark = new ChatMessageResp.MessageMark();
        mark.setUserLike(Optional.ofNullable(receiveUid)
                .filter(uid -> likeMarks.stream().anyMatch(messageMark -> Objects.equals(messageMark.getUid(), uid)))
                .map(a -> YesOrNoEnum.YES.getStatus())
                .orElse(YesOrNoEnum.NO.getStatus()));
        mark.setUserDislike(Optional.ofNullable(receiveUid)
                .filter(uid -> dislikeMarks.stream().anyMatch(messageMark -> Objects.equals(messageMark.getUid(), uid)))
                .map(a -> YesOrNoEnum.YES.getStatus())
                .orElse(YesOrNoEnum.NO.getStatus()));
        mark.setLikeCount(likeMarks.size());
        mark.setDislikeCount(dislikeMarks.size());
        return mark;
    }

    public static ChatMessageReq buildTextMsg(Long uid, String msg, Long roomId) {
        return null;
    }

    public static ChatMessageReq buildAgreeMsg(Long roomId) {
        ChatMessageReq chatMessageReq = new ChatMessageReq();
        chatMessageReq.setMsgType(MessageTypeEnum.TEXT.getType());
        chatMessageReq.setRoomId(roomId);
        chatMessageReq.setBody(TextMsgResp.builder().content("我们已经是好友了，开始聊天吧").build());
        return chatMessageReq;
    }

    public static MessageExtra buildRecallExtra(Long recallUid, Date date) {
        return MessageExtra.builder()
                .recall(MsgRecall.builder().recallUid(recallUid).recallTime(date).build())
                .build();
    }
}

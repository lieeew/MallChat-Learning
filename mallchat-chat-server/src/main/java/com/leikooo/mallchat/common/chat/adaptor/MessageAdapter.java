package com.leikooo.mallchat.common.chat.adaptor;

import com.leikooo.mallchat.common.chat.domain.entity.Message;
import com.leikooo.mallchat.common.chat.domain.entity.MessageMark;
import com.leikooo.mallchat.common.chat.domain.enums.MessageMarkTypeEnum;
import com.leikooo.mallchat.common.chat.domain.enums.MessageTypeEnum;
import com.leikooo.mallchat.common.chat.domain.vo.request.ChatMessageReq;
import com.leikooo.mallchat.common.chat.domain.vo.response.ChatMessageResp;
import com.leikooo.mallchat.common.chat.domain.vo.response.msg.TextMsgResp;
import com.leikooo.mallchat.common.chat.service.factory.MsgHandlerFactory;
import com.leikooo.mallchat.common.common.domain.enums.YesOrNoEnum;

import java.util.List;
import java.util.Map;
import java.util.Objects;
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
        messageVO.setBody((MsgHandlerFactory.getStrategyNoNull(message.getType())).showMsg());
        messageVO.setMessageMark(buildMessageMark(message.getId(), marks, receiveUid));
        return messageVO;
    }

    private static ChatMessageResp.UserInfo buildFromUserInfo(Long uid) {
        return ChatMessageResp.UserInfo.builder().uid(uid).build();
    }

    private static ChatMessageResp.MessageMark buildMessageMark(Long messageId, List<MessageMark> marks, Long receiveUid) {
        Map<Long, List<MessageMark>> markMap = marks.stream().collect(Collectors.groupingBy(MessageMark::getMsgId));
        ChatMessageResp.MessageMark messageMark = new ChatMessageResp.MessageMark();
        markMap.forEach((msgId, msgMark) -> {
            if (!Objects.equals(msgId, messageId)) {
                return;
            }
            msgMark.forEach(mark -> {
                if (Objects.equals(mark.getStatus(), YesOrNoEnum.YES.getStatus())) {
                    // 消息失效
                    return;
                }
                if (Objects.equals(mark.getType(), MessageMarkTypeEnum.LIKE.getType())) {
                    messageMark.setLikeCount(messageMark.getLikeCount() + 1);
                }
                if (Objects.equals(mark.getType(), MessageMarkTypeEnum.REPORT.getType())) {
                    messageMark.setDislikeCount(messageMark.getDislikeCount() + 1);
                }
                if (Objects.nonNull(receiveUid) && Objects.equals(mark.getUid(), receiveUid)) {
                    if (Objects.equals(mark.getType(), MessageMarkTypeEnum.LIKE.getType())) {
                        messageMark.setUserLike(YesOrNoEnum.YES.getStatus());
                    }
                    if (Objects.equals(mark.getType(), MessageMarkTypeEnum.REPORT.getType())) {
                        messageMark.setUserDislike(YesOrNoEnum.YES.getStatus());
                    }
                }
            });
        });
        return messageMark;
    }

    public static ChatMessageReq buildTextMsg(Long uid, String msg, Long roomId) {
        return null;
    }

    public static ChatMessageReq buildAgreeMsg(Long uid, Long roomId) {
        ChatMessageReq chatMessageReq = new ChatMessageReq();
        chatMessageReq.setMsgType(MessageTypeEnum.TEXT.getType());
        chatMessageReq.setRoomId(roomId);
        chatMessageReq.setBody(TextMsgResp.builder().content("我们已经是好友了，开始聊天吧").build());
        return chatMessageReq;
    }
}

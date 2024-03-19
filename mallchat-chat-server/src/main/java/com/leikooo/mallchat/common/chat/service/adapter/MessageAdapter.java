package com.leikooo.mallchat.common.chat.service.adapter;

import com.leikooo.mallchat.common.chat.domain.entity.Message;
import com.leikooo.mallchat.common.chat.domain.entity.MessageMark;
import com.leikooo.mallchat.common.chat.domain.vo.response.ChatMessageResp;
import com.leikooo.mallchat.common.common.domain.enums.YesOrNoEnum;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/3/18
 * @description
 */
public class MessageAdapter {

    public static List<ChatMessageResp> buildMsgResp(List<MessageMark> messageMarks, List<Message> messages) {
        Map<Long, MessageMark> messageMarkMap = messageMarks.stream().collect(Collectors.toMap(MessageMark::getMsgId, Function.identity()));
        return messages.stream().map(message -> {
            ChatMessageResp chatMessageResp = new ChatMessageResp();
            MessageMark messageMark = messageMarkMap.get(message.getId());
            ChatMessageResp.MessageMark mark = ChatMessageResp.MessageMark.builder()
                    .likeCount(0)
                    .userLike(YesOrNoEnum.NO.getStatus())
                    .userDislike(YesOrNoEnum.NO.getStatus())
                    .build();
            chatMessageResp.setMessage(buildMessage(message, messageMarks, messageMark.getUid()));
            return chatMessageResp;
        }).collect(Collectors.toList());
    }

    private static ChatMessageResp.Message buildMessage(Message message, List<MessageMark> marks, Long receiveUid) {
        Map<Long, List<MessageMark>> markMap = marks.stream().collect(Collectors.groupingBy(MessageMark::getMsgId));
        ChatMessageResp.Message messageVO = ChatMessageResp.Message.builder().roomId(message.getRoomId()).type(message.getType()).build();
        return messageVO;
    }

    private static ChatMessageResp.UserInfo buildFromUserInfo(Message message) {
        return ChatMessageResp.UserInfo.builder().uid(message.getFromUid()).build();
    }
    
}

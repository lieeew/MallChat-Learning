package com.leikooo.mallchat.common.chat.service;

import com.leikooo.mallchat.common.chat.domain.entity.Message;
import com.leikooo.mallchat.common.chat.domain.vo.request.ChatMessageReq;
import com.leikooo.mallchat.common.chat.domain.vo.response.ChatMessageResp;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/3/11
 * @description
 */
public interface ChatService {
    void sendMsg(Long uid, ChatMessageReq req);

    ChatMessageResp getChatMessageResp(Message message, Long userId) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException;

    List<ChatMessageResp> getMsgRespBatch(List<Message> messages, Long receiveUid);
}

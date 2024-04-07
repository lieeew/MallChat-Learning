package com.leikooo.mallchat.common.chat.service;

import com.leikooo.mallchat.common.chat.domain.entity.Message;
import com.leikooo.mallchat.common.chat.domain.vo.request.ChatMessageBaseReq;
import com.leikooo.mallchat.common.chat.domain.vo.request.ChatMessageMarkReq;
import com.leikooo.mallchat.common.chat.domain.vo.request.ChatMessagePageReq;
import com.leikooo.mallchat.common.chat.domain.vo.request.ChatMessageReq;
import com.leikooo.mallchat.common.chat.domain.vo.response.ChatMessageResp;
import com.leikooo.mallchat.common.common.domain.vo.response.CursorPageBaseResp;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/3/11
 * @description
 */
public interface ChatService {
    void sendMsg(Long uid, ChatMessageReq req);

    /**
     * @param receiveUid 接受消息的 uid，可 null
     */
    ChatMessageResp getChatMessageResp(Message message, Long receiveUid);

    List<ChatMessageResp> getMsgRespBatch(List<Message> messages, Long receiveUid);

    CursorPageBaseResp<ChatMessageResp> getMsgPage(ChatMessagePageReq req, Long uid);

    /**
     * 撤回消息接口
     */
    void recallMsg(ChatMessageBaseReq req, Long uid);

    void setMsgMark(Long uid, ChatMessageMarkReq request);
}

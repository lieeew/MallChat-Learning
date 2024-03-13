package com.leikooo.mallchat.common.chat.service;

import com.leikooo.mallchat.common.chat.domain.vo.request.ChatMessageReq;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/3/11
 * @description
 */
public interface ChatService {
    void sendMsg(Long uid, ChatMessageReq req);
}

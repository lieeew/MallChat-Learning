package com.leikooo.mallchat.common.common.event;

import com.leikooo.mallchat.common.chat.domain.dto.ChatMsgRecallDTO;
import com.leikooo.mallchat.common.chat.domain.entity.Message;
import com.leikooo.mallchat.common.chat.service.impl.ChatServiceImpl;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/3/24
 * @description
 */
@Getter
public class MessageRecallEvent extends ApplicationEvent {

    private final ChatMsgRecallDTO chatMsgRecallDTO;

    public MessageRecallEvent(Object o, ChatMsgRecallDTO chatMsgRecallDTO) {
        super(o);
        this.chatMsgRecallDTO = chatMsgRecallDTO;
    }
}

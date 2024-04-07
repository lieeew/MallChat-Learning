package com.leikooo.mallchat.common.common.event;

import com.leikooo.mallchat.common.chat.domain.dto.ChatMessageMarkDTO;
import com.leikooo.mallchat.common.chat.domain.dto.ChatMsgRecallDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/3/24
 * @description
 */
@Getter
public class MessageMarkEvent extends ApplicationEvent {

    private final ChatMessageMarkDTO chatMessageMarkDTO;

    public MessageMarkEvent(Object o, ChatMessageMarkDTO chatMessageMarkDTO) {
        super(o);
        this.chatMessageMarkDTO = chatMessageMarkDTO;
    }
}

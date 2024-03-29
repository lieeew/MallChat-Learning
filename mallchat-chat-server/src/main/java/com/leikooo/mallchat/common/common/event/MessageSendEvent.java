package com.leikooo.mallchat.common.common.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/3/9
 * @description
 */
@Getter
public class MessageSendEvent extends ApplicationEvent {
    public MessageSendEvent(Object source, Long messageId) {
        super(source);
        this.messageId = messageId;
    }

    private final Long messageId;
}

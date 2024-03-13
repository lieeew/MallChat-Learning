package com.leikooo.mallchat.common.common.event.listenser;

import com.leikooo.mallchat.common.common.event.MessageSendEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/2/22
 * @description
 */
@Component
public class MessageSendListener {

    @EventListener(classes = MessageSendEvent.class)
    public void receiveBadge(MessageSendEvent event) {
        Long roomId = event.getRoomId();
        // todo 发送到 MQ
    }
}

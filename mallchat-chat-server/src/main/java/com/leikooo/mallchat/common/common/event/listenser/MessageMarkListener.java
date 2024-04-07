package com.leikooo.mallchat.common.common.event.listenser;

import com.leikooo.mallchat.common.common.event.MessageMarkEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/2/22
 * @description
 */
@Component
public class MessageMarkListener {
    @EventListener(MessageMarkEvent.class)
    public void consumerMessage(MessageMarkEvent messageMarkEvent) {

    }
 }

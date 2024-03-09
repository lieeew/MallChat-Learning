package com.leikooo.mallchat.common.common.event;

import com.leikooo.mallchat.common.user.domain.entity.UserBackpack;
import com.leikooo.mallchat.common.user.service.impl.UserBackpackServiceImpl;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/3/9
 * @description
 */
@Getter
public class ItemReceiveEvent extends ApplicationEvent {
    public ItemReceiveEvent(Object source, UserBackpack userBackpack) {
        super(source);
        this.userBackpack = userBackpack;
    }

    private final UserBackpack userBackpack;
}

package com.leikooo.mallchat.common.common.event;

import com.leikooo.mallchat.common.user.domain.entity.User;
import com.leikooo.mallchat.common.user.domain.entity.UserApply;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/2/22
 * @description
 */
@Getter
public class UserApplyEvent extends ApplicationEvent {
    private final UserApply userApply;

    public UserApplyEvent(Object source, UserApply userApply) {
        super(source);
        this.userApply = userApply;
    }
}

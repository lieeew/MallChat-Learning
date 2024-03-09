package com.leikooo.mallchat.common.common.event.listenser;

import com.leikooo.mallchat.common.common.event.ItemReceiveEvent;
import com.leikooo.mallchat.common.common.event.UserApplyEvent;
import com.leikooo.mallchat.common.user.adapter.WebSocketAdapter;
import com.leikooo.mallchat.common.user.dao.UserApplyDao;
import com.leikooo.mallchat.common.user.domain.entity.UserApply;
import com.leikooo.mallchat.common.user.domain.entity.UserBackpack;
import com.leikooo.mallchat.common.user.service.WebSocketService;
import com.leikooo.mallchat.common.user.service.cache.UserCache;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/2/22
 * @description
 */
@Component
public class ItemReceiveListener {
    @Resource
    private UserCache userCache;

    @EventListener(classes = ItemReceiveEvent.class)
    public void receiveBadge(ItemReceiveEvent event) {
        UserBackpack userBackpack = event.getUserBackpack();
        userCache.userInfoChange(userBackpack.getUid());
    }
}

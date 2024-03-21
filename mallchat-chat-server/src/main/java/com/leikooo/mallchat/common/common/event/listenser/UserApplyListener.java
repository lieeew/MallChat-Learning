package com.leikooo.mallchat.common.common.event.listenser;

import com.leikooo.mallchat.common.chat.adaptor.MessageAdapter;
import com.leikooo.mallchat.common.chat.service.ChatService;
import com.leikooo.mallchat.common.common.event.UserApplyEvent;
import com.leikooo.mallchat.common.user.adapter.WebSocketAdapter;
import com.leikooo.mallchat.common.user.dao.UserApplyDao;
import com.leikooo.mallchat.common.user.domain.entity.UserApply;
import com.leikooo.mallchat.common.user.domain.vo.response.ws.WSFriendApply;
import com.leikooo.mallchat.common.user.service.WebSocketService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/2/22
 * @description
 */
@Component
public class UserApplyListener {
    @Resource
    private UserApplyDao userApplyDao;

    @Resource
    private WebSocketService webSocketService;

    @EventListener(UserApplyEvent.class)
    public void countUserApplyUnRead(UserApplyEvent userApplyEvent) {
        UserApply userApply = userApplyEvent.getUserApply();
        int unReadCount = userApplyDao.getUnReadCount(userApply.getTargetId());
        Long uid = userApply.getUid();
        webSocketService.sendToUid(WebSocketAdapter.buildApplySend(unReadCount, uid), userApply.getTargetId());
    }
}

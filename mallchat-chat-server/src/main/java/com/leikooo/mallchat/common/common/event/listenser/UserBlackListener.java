package com.leikooo.mallchat.common.common.event.listenser;

import com.leikooo.mallchat.common.common.domain.enums.YesOrNoEnum;
import com.leikooo.mallchat.common.common.event.UserBlackEvent;
import com.leikooo.mallchat.common.user.adapter.UserAdaptor;
import com.leikooo.mallchat.common.user.adapter.WebSocketAdapter;
import com.leikooo.mallchat.common.user.dao.UserDao;
import com.leikooo.mallchat.common.user.dao.UserFriendDao;
import com.leikooo.mallchat.common.user.domain.entity.User;
import com.leikooo.mallchat.common.user.domain.entity.UserFriend;
import com.leikooo.mallchat.common.user.service.WebSocketService;
import com.leikooo.mallchat.common.user.service.cache.UserCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.annotation.Resource;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/2/22
 * @description
 */
@Slf4j
@Component
public class UserBlackListener {
    @Resource
    private WebSocketService webSocketService;

    @Resource
    private UserDao userDao;

    @Resource
    private UserCache userCache;

    @Resource
    private UserFriendDao userFriendDao;

    @Async
    @TransactionalEventListener(value = UserBlackEvent.class, phase = TransactionPhase.AFTER_COMMIT)
    public void sendMsg(UserBlackEvent event) {
        User blockUser = event.getUser();
        webSocketService.sendToAllOnline(WebSocketAdapter.buildBlackResp(blockUser.getId()));
    }

    @Async
    @TransactionalEventListener(value = UserBlackEvent.class, phase = TransactionPhase.AFTER_COMMIT)
    public void changeUserStatus(UserBlackEvent event) {
        User blockUser = event.getUser();
        userDao.updateById(UserAdaptor.buildBlackUser(blockUser));
    }

    @Async
    @TransactionalEventListener(value = UserBlackEvent.class, phase = TransactionPhase.AFTER_COMMIT)
    public void evictBlackUserCache(UserBlackEvent event) {
        userCache.evictBlackMap();
    }

    /**
     * 删除被拉黑的用户的所有好友
     * todo 同时删除 ta 在的所有的群
     *
     * @param event
     */
    @Async
    @TransactionalEventListener(value = UserBlackEvent.class, phase = TransactionPhase.AFTER_COMMIT)
    public void deleteBlackUserFriends(UserBlackEvent event) {
        User user = event.getUser();
        userFriendDao.deleteUserAllFriends(user.getId());
        userCache.userInfoChange(user.getId());
    }
}

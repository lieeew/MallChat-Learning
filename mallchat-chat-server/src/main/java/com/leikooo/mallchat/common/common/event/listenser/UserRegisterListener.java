package com.leikooo.mallchat.common.common.event.listenser;

import com.leikooo.mallchat.common.common.domain.enums.IdempotentEnum;
import com.leikooo.mallchat.common.common.event.UserRegisterEvent;
import com.leikooo.mallchat.common.user.dao.UserDao;
import com.leikooo.mallchat.common.user.domain.entity.User;
import com.leikooo.mallchat.common.user.domain.enums.ItemEnum;
import com.leikooo.mallchat.common.user.service.UserBackpackService;
import com.leikooo.mallchat.common.user.service.cache.UserCache;
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
@Component
public class UserRegisterListener {
    @Resource
    private UserBackpackService userBackpackService;

    @Resource
    private UserDao userDao;

    @Resource
    private UserCache userCache;

    /**
     * 这个是不是加 @Async 依据的是这个发这个改名卡是不是重要
     * 如果重要的话那么就不在事务里面执行，如果在事务里面执行的话
     * 这个发放改名卡的逻辑出现异常的话，那么这个事务就会回滚，就会导致 「注册」 失败
     * 我们认为这个发卡的逻辑其实是不如注册重要的，所以我们加了 @Async，并且我们使用了 TransactionPhase.AFTER_COMMIT
     */
    @Async
    @TransactionalEventListener(value = UserRegisterEvent.class, phase = TransactionPhase.AFTER_COMMIT)
    public void senCard(UserRegisterEvent event) {
        User user = event.getUser();
        userBackpackService.acquireItem(user.getId(), ItemEnum.MODIFY_NAME_CARD.getId(), IdempotentEnum.UID.getType(), user.getId().toString());
    }

    @TransactionalEventListener(value = UserRegisterEvent.class, phase = TransactionPhase.AFTER_COMMIT)
    public void sendBadges(UserRegisterEvent event) {
        User user = event.getUser();
        int countNum = userDao.count();
        if (countNum < 10) {
            userBackpackService.acquireItem(user.getId(), ItemEnum.REG_TOP100_BADGE.getId(), IdempotentEnum.UID.getType(), user.getId().toString());
            return;
        }
        if (countNum < 100) {
            userBackpackService.acquireItem(user.getId(), ItemEnum.REG_TOP100_BADGE.getId(), IdempotentEnum.UID.getType(), user.getId().toString());
        }
    }

    @TransactionalEventListener(value = UserRegisterEvent.class, phase = TransactionPhase.AFTER_COMMIT)
    public void saveModifyTime(UserRegisterEvent event) {
        User user = event.getUser();
        long time = user.getUpdateTime().getTime();
        userCache.refreshUserModifyTime(user.getId(), time);
    }
}

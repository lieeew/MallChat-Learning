package com.leikooo.mallchat.common.common.event.listenser;

import com.leikooo.mallchat.common.common.domain.enums.IdempotentEnum;
import com.leikooo.mallchat.common.common.event.UserRegisterEvent;
import com.leikooo.mallchat.common.user.dao.UserDao;
import com.leikooo.mallchat.common.user.domain.entity.User;
import com.leikooo.mallchat.common.user.domain.entity.UserBackpack;
import com.leikooo.mallchat.common.user.domain.enums.ItemEnum;
import com.leikooo.mallchat.common.user.domain.enums.ItemTypeEnum;
import com.leikooo.mallchat.common.user.service.UserBackpackService;
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

    @TransactionalEventListener(value = UserRegisterListener.class, phase = TransactionPhase.AFTER_COMMIT)
    public void senCard(UserRegisterEvent event) {
        User user = event.getUser();
        userBackpackService.acquireItem(user.getId(), ItemEnum.MODIFY_NAME_CARD.getId(), IdempotentEnum.UID.getType(), user.getId().toString());
    }

    @TransactionalEventListener(value = UserRegisterListener.class, phase = TransactionPhase.AFTER_COMMIT)
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
}

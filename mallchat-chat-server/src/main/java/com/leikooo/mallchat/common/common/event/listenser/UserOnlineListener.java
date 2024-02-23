package com.leikooo.mallchat.common.common.event.listenser;

import com.leikooo.mallchat.common.common.event.UserOnlineEvent;
import com.leikooo.mallchat.common.user.dao.UserDao;
import com.leikooo.mallchat.common.user.domain.entity.User;
import com.leikooo.mallchat.common.user.domain.enums.ChatActiveStatusEnum;
import com.leikooo.mallchat.common.user.service.IpService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/2/22
 * @description
 */
@Component
public class UserOnlineListener {
    @Resource
    private UserDao userDao;

    @Resource
    private IpService ipService;

    @Async
    @EventListener(value = UserOnlineEvent.class)
    public void saveDb(UserOnlineEvent event) {
        User user = event.getUser();
        User.builder()
                .id(user.getId())
                .ipInfo(user.getIpInfo())
                .updateTime(user.getUpdateTime())
                .activeStatus(ChatActiveStatusEnum.ONLINE.getStatus())
                .lastOptTime(user.getLastOptTime())
                .build();
        userDao.updateById(user);
        // 调用 IPService
        ipService.refreshIpDetailAsync(user.getId());
    }
}

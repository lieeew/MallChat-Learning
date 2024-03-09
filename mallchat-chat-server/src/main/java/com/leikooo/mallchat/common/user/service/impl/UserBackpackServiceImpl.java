package com.leikooo.mallchat.common.user.service.impl;

import cn.hutool.extra.spring.SpringUtil;
import com.leikooo.mallchat.common.common.annotation.RedissonLock;
import com.leikooo.mallchat.common.common.event.ItemReceiveEvent;
import com.leikooo.mallchat.common.user.adapter.UserBackpackAdaptor;
import com.leikooo.mallchat.common.user.dao.UserBackpackDao;
import com.leikooo.mallchat.common.user.domain.entity.ItemConfig;
import com.leikooo.mallchat.common.user.domain.entity.UserBackpack;
import com.leikooo.mallchat.common.user.domain.enums.ItemTypeEnum;
import com.leikooo.mallchat.common.user.service.UserBackpackService;
import com.leikooo.mallchat.common.user.service.cache.ItemCache;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/2/19
 * @description
 */
@Service
public class UserBackpackServiceImpl implements UserBackpackService {
    @Resource
    private UserBackpackDao userBackpackDao;

    @Resource
    private ItemCache itemCache;

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void acquireItem(Long uid, Long itemId, Integer messageType, String businessId) {
        String idempotentId = getIdempotent(itemId, messageType, businessId);
        // 1、可以自己注入自己然后调用 @Lazy @Resource private UserBackpackServiceImpl userBackpackServiceImpl;
        // 2、可以使用 ((UserBackpackServiceImpl) AopContext.currentProxy()).doAcquireItem(uid, itemId, idempotentId);
        // 3、使用 SpringUtil 获取 bean 调用
        SpringUtil.getBean(this.getClass()).doAcquireItem(uid, itemId, idempotentId);
    }

    @RedissonLock(key = "#idempotentId", waitTime = 5000)
    public void doAcquireItem(Long uid, Long itemId, String idempotentId) {
        UserBackpack userBackpack = userBackpackDao.getByIdempotent(idempotentId);
        if (Objects.nonNull(userBackpack)) {
            return;
        }
        // 业务检查
        ItemConfig itemConfig = itemCache.getById(itemId);
        // 徽章类型做唯一性检查
        if (ItemTypeEnum.BADGE.getType().equals(itemConfig.getType())) {
            Integer countByValidItemId = userBackpackDao.getCountByValidItemId(uid, itemId);
            if (countByValidItemId > 0) {
                // 已经有徽章了不发
                return;
            }
        }
        UserBackpack insert = UserBackpackAdaptor.buildNewUserBackpack(uid, itemId, idempotentId);
        userBackpackDao.save(insert);
        // 发送时间
        applicationEventPublisher.publishEvent(new ItemReceiveEvent(this, insert));
    }

    /**
     * 获取幂等号
     *
     * @param itemId
     * @param messageType
     * @param businessId
     * @return
     */
    private String getIdempotent(Long itemId, Integer messageType, String businessId) {
        // 幂等号 = itemId + source + businessId
        return String.format("%d_%d_%s", itemId, messageType, businessId);
    }
}

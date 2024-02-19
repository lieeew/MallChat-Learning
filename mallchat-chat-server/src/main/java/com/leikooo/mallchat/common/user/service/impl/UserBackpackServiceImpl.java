package com.leikooo.mallchat.common.user.service.impl;

import com.leikooo.mallchat.common.common.service.LockService;
import com.leikooo.mallchat.common.common.util.AssertUtil;
import com.leikooo.mallchat.common.user.adapter.UserBackpackAdaptor;
import com.leikooo.mallchat.common.user.dao.UserBackpackDao;
import com.leikooo.mallchat.common.user.domain.entity.UserBackpack;
import com.leikooo.mallchat.common.user.service.UserBackpackService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
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
    private LockService lockService;

    @Resource
    private UserBackpackDao userBackpackDao;

    @Override
    public void acquireItem(Long uid, Long itemId, Integer messageType, String businessId) {
        String idempotentId = getIdempotent(itemId, messageType, businessId);
        lockService.executeWithLock(idempotentId, () -> {
            UserBackpack userBackpack = userBackpackDao.getByIdempotent(idempotentId);
            if (Objects.nonNull(userBackpack)) {
                return;
            }
            // 这里还可以进行业务检查
            // 我们这里的改名卡不太重要就直接操作了
            userBackpackDao.save(UserBackpackAdaptor.buildNewUserBackpack(uid, itemId, idempotentId));
        });
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

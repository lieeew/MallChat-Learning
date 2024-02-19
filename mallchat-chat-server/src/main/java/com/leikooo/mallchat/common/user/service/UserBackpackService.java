package com.leikooo.mallchat.common.user.service;


import com.leikooo.mallchat.common.common.domain.enums.IdempotentEnum;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户背包表 服务类
 * </p>
 *
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date  2024-02-14
 */
public interface UserBackpackService {
    /**
     * 获取物品
     * @param uid
     * @param itemId
     * @param messageType @see com.leikooo.mallchat.common.common.domain.enums.IdempotentEnum
     * @param businessId
     */
    void acquireItem(Long uid, Long itemId, Integer messageType, String businessId);
}

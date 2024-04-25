package com.leikooo.mallchat.common.user.service;


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
     * @param uid 用户 id
     * @param itemId 物品 id
     * @param idempotentType look see
     * @see com.leikooo.mallchat.common.common.domain.enums.IdempotentEnum
     * @param businessId if use UID this is userId that String type
     */
    void acquireItem(Long uid, Long itemId, Integer idempotentType, String businessId);
}

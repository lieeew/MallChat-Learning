package com.leikooo.mallchat.common.user.adapter;

import com.leikooo.mallchat.common.common.domain.enums.YesOrNoEnum;
import com.leikooo.mallchat.common.user.domain.entity.UserBackpack;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/2/19
 * @description
 */
public class UserBackpackAdaptor {
    public static UserBackpack buildNewUserBackpack(Long uid, Long itemId, String idempotentId) {
        return UserBackpack.builder()
                .uid(uid)
                .idempotent(idempotentId)
                .itemId(itemId)
                .status(YesOrNoEnum.NO.getStatus())
                .build();
    }
}

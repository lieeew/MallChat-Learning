package com.leikooo.mallchat.common.user.adapter;

import com.leikooo.mallchat.common.common.domain.enums.YesOrNoEnum;
import com.leikooo.mallchat.common.user.domain.entity.UserFriend;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/3/2
 * @description userFriendAdapter 适配器
 */
public class UserFriendAdapter {
    public static UserFriend buildUserFriend(Long targetUid, Long applyId) {
        return UserFriend.builder()
                .friendUid(targetUid)
                .uid(applyId)
                .deleteStatus(YesOrNoEnum.NO.getStatus())
                .build();
    }
}

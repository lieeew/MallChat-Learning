package com.leikooo.mallchat.common.user.adapter;

import com.leikooo.mallchat.common.user.domain.entity.UserApply;
import com.leikooo.mallchat.common.user.domain.enums.ApplyReadStatusEnum;
import com.leikooo.mallchat.common.user.domain.enums.ApplyStatusEnum;
import com.leikooo.mallchat.common.user.domain.enums.ApplyTypeEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/3/1
 * @description
 */
public class UserApplyAdapter {
    public static UserApply buildUserApply(@NotNull Long uid, @NotNull Long targetUid, @NotBlank String msg) {
        return UserApply.builder()
                .uid(uid)
                .targetId(targetUid)
                .msg(msg)
                .readStatus(ApplyReadStatusEnum.UNREAD.getCode())
                .status(ApplyStatusEnum.WAIT_APPROVAL.getCode())
                .type(ApplyTypeEnum.ADD_FRIEND.getCode())
                .build();
    }
}

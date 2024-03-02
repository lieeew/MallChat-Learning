package com.leikooo.mallchat.common.user.domain.enums;

import lombok.Getter;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/3/2
 * @description
 */
@Getter
public enum UserStatusEnum {
    NORMAL(0, "正常"),

    BLACK(1, "黑名单"),
    ;
    private final Integer status;

    private final String desc;
    UserStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }
}

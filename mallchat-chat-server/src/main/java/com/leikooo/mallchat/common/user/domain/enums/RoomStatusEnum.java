package com.leikooo.mallchat.common.user.domain.enums;

import lombok.Getter;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/3/3
 * @description
 */
@Getter
public enum RoomStatusEnum {
    NORMAL(0, "正常"),
    DISABLE(1, "禁用"),
    ;
    private final Integer status;

    private final String desc;

    RoomStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }
}

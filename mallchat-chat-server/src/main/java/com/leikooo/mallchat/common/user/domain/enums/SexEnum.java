package com.leikooo.mallchat.common.user.domain.enums;

import lombok.Getter;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/3/2
 * @description
 */
@Getter
public enum SexEnum {
    MALE(1, "男"),

    FEMALE(2, "女"),
    ;
    private final Integer code;

    private final String desc;

    SexEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}

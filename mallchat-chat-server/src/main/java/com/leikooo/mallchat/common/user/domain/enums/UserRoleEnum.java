package com.leikooo.mallchat.common.user.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description: 角色枚举
 * @author  <a href="https://github.com/lieeew">leikooo</a>
 * Date: 2023-03-19
 */
@AllArgsConstructor
@Getter
public enum UserRoleEnum {
    ADMIN(1L, "超级管理员"),
    CHAT_MANAGER(2L, "抹茶群聊管理"),
    ;

    private final Long code;
    private final String desc;

    private static Map<Long, UserRoleEnum> cache;

    static {
        cache = Arrays.stream(UserRoleEnum.values()).collect(Collectors.toMap(UserRoleEnum::getCode, Function.identity()));
    }

    public static UserRoleEnum of(Long type) {
        return cache.get(type);
    }
}

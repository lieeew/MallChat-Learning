package com.leikooo.mallchat.common.user.domain.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/3/3
 * @description
 */
@Getter
public enum RoomTypeEnum {
    FRIEND(2, "单聊"),
    GROUP(1, "群聊"),
    ;
    private final Integer type;

    private final String desc;

    private static Map<Integer, RoomTypeEnum> cache;

    RoomTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    static {
        cache = Arrays.stream(RoomTypeEnum.values()).collect(Collectors.toMap(RoomTypeEnum::getType, Function.identity()));
    }

    public static RoomTypeEnum of(Integer type) {
        return cache.get(type);
    }
}

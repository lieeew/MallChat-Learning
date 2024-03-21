package com.leikooo.mallchat.common.chat.domain.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/3/12
 * @description
 */
@Getter
public enum MessageType {
    NORMAL_MESSAGE(1, "正常消息"),

    RECALL_MESSAGE(1, "撤回消息"),
    ;

    private final Integer type;

    private final String desc;

    private static Map<Integer, MessageType> cache;

    MessageType(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    static {
        cache = Arrays.stream(MessageType.values()).collect(Collectors.toMap(MessageType::getType, Function.identity()));
    }

    public static MessageType of(Integer type) {
        return cache.get(type);
    }
}

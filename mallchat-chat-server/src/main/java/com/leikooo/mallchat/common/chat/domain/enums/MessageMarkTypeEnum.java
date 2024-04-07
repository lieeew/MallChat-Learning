package com.leikooo.mallchat.common.chat.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description: 消息标记类型
 * @author : <a href="https://github.com/zongzibinbin">abin</a>
 * Date: 2023-03-19
 */
@AllArgsConstructor
@Getter
public enum MessageMarkTypeEnum {
    REPORT(0, "举报", 5),
    LIKE(1, "点赞", 10),
    DISLIKE(2, "点踩", 10),
    ;

    private final Integer type;
    private final String desc;
    private final Integer riseNum; // 需要多少个标记升级

    private static Map<Integer, MessageMarkTypeEnum> cache;

    static {
        cache = Arrays.stream(MessageMarkTypeEnum.values()).collect(Collectors.toMap(MessageMarkTypeEnum::getType, Function.identity()));
    }

    public static MessageMarkTypeEnum of(Integer type) {
        return cache.get(type);
    }
}

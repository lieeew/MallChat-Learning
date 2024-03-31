package com.leikooo.mallchat.common.chat.domain.enums;

import com.leikooo.mallchat.common.chat.domain.entity.msg.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description: 消息状态
 * @author : <a href="https://github.com/zongzibinbin">abin</a>
 * Date: 2023-03-19
 */
@AllArgsConstructor
@Getter
public enum MessageTypeEnum {
    TEXT(1, "正常消息", TextMsgDTO.class),
    RECALL(2, "撤回消息", MsgRecall.class),
    IMG(3, "图片", ImgMsgDTO.class),
    FILE(4, "文件", FileMsgDTO.class),
    SOUND(5, "语音", SoundMsgDTO.class),
    VIDEO(6, "视频", VideoMsgDTO.class),
    EMOJI(7, "表情", EmojisMsgDTO.class),
    SYSTEM(8, "系统消息", Object.class),
    ;

    private final Integer type;

    private final String desc;

    private final Class<?> clazz;

    private static Map<Integer, MessageTypeEnum> cache;

    static {
        cache = Arrays.stream(MessageTypeEnum.values()).collect(Collectors.toMap(MessageTypeEnum::getType, Function.identity()));
    }

    public static MessageTypeEnum of(Integer type) {
        return cache.get(type);
    }
}

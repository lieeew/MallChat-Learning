package com.leikooo.mallchat.common.chat.service.factory;


import com.leikooo.mallchat.common.chat.domain.enums.MessageMarkTypeEnum;
import com.leikooo.mallchat.common.chat.service.strategy.mark.AbstractMsgMarkStrategy;
import com.leikooo.mallchat.common.common.utils.AssertUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/4/7
 * @description
 */
public class MarkMsgFactory {
    public static Map<Integer, AbstractMsgMarkStrategy> msgMarkStrategyMap = new ConcurrentHashMap<>();

    public static AbstractMsgMarkStrategy getStrategyNoNull(MessageMarkTypeEnum msgMarkType) {
        AbstractMsgMarkStrategy abstractMsgMarkStrategy = msgMarkStrategyMap.get(msgMarkType.getType());
        AssertUtil.isNotEmpty(abstractMsgMarkStrategy, "Mark 策略类无法识别");
        return abstractMsgMarkStrategy;
    }
}

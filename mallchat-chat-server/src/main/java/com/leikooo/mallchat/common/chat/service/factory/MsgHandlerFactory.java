package com.leikooo.mallchat.common.chat.service.factory;

import com.leikooo.mallchat.common.chat.service.strategy.msg.AbstractMsgHandler;
import com.leikooo.mallchat.common.common.exception.CommonErrorEnum;
import com.leikooo.mallchat.common.common.utils.AssertUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/3/11
 * @description
 */
public class MsgHandlerFactory {
    public static Map<Integer, AbstractMsgHandler<?>> msgHandlerMap = new ConcurrentHashMap<>();

    public static void register(Integer type, AbstractMsgHandler<?> handler) {
        msgHandlerMap.put(type, handler);
    }

    public static AbstractMsgHandler<?> getStrategyNoNull(Integer markType) {
        AbstractMsgHandler<?> strategy = msgHandlerMap.get(markType);
        AssertUtil.isNotEmpty(strategy, CommonErrorEnum.SYSTEM_ERROR);
        return strategy;
    }
}

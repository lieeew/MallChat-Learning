package com.leikooo.mallchat.common.chat.service.strategy.mark;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/4/7
 * @description
 */
public interface MsgMarkStrategy {
    void doMark(Long messageId, Long uid);

    void unMark(Long messageId, Long uid);

    void execute(Long messageId, Long uid, Integer actType);
}


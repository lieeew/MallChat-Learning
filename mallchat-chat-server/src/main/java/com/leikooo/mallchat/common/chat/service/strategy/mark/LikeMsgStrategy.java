package com.leikooo.mallchat.common.chat.service.strategy.mark;

import com.leikooo.mallchat.common.chat.domain.enums.MessageMarkTypeEnum;
import com.leikooo.mallchat.common.chat.service.factory.MarkMsgFactory;
import org.springframework.stereotype.Component;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/4/7
 * @description
 */
@Component
public class LikeMsgStrategy extends AbstractMsgMarkStrategy {
    @Override
    public MessageMarkTypeEnum getMessageMarkEnum() {
        return MessageMarkTypeEnum.LIKE;
    }

    @Override
    public void doMark(Long messageId, Long uid) {
        super.doMark(messageId, uid);
        MarkMsgFactory.getStrategyNoNull(MessageMarkTypeEnum.DISLIKE).unMark(messageId, uid);
    }
}

package com.leikooo.mallchat.common.chat.service.strategy.msg;

import com.leikooo.mallchat.common.chat.domain.entity.Message;
import com.leikooo.mallchat.common.chat.domain.entity.msg.MessageExtra;
import com.leikooo.mallchat.common.chat.domain.entity.msg.MsgRecall;
import com.leikooo.mallchat.common.chat.domain.entity.msg.SoundMsgDTO;
import com.leikooo.mallchat.common.chat.domain.enums.MessageTypeEnum;
import com.leikooo.mallchat.common.chat.service.cache.MsgCache;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/3/18
 * @description
 */
@Component
public class RecallMsgHandler extends AbstractMsgHandler<MsgRecall> {
    @Override
    protected MessageTypeEnum getMessageType() {
        return MessageTypeEnum.RECALL;
    }

    @Override
    public void saveMsg(Message msg, MsgRecall body) {
        MessageExtra messageExtra = MessageExtra.builder().recall(Optional.ofNullable(body).orElse(new MsgRecall())).build();
        Message message = new Message();
        BeanUtils.copyProperties(msg, message);
        message.setExtra(messageExtra);
        messageDao.updateById(message);
    }

    @Override
    public Object showMsg(Message message) {
        return message.getExtra().getRecall();
    }

}

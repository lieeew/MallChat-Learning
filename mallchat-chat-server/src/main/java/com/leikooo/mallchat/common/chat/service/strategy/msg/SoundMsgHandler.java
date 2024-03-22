package com.leikooo.mallchat.common.chat.service.strategy.msg;

import com.leikooo.mallchat.common.chat.domain.entity.Message;
import com.leikooo.mallchat.common.chat.domain.entity.msg.MessageExtra;
import com.leikooo.mallchat.common.chat.domain.entity.msg.SoundMsgDTO;
import com.leikooo.mallchat.common.chat.domain.enums.MessageTypeEnum;
import org.springframework.beans.BeanUtils;

import java.util.Optional;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/3/18
 * @description
 */
public class SoundMsgHandler extends AbstractMsgHandler<SoundMsgDTO> {
    @Override
    protected MessageTypeEnum getMessageType() {
        return MessageTypeEnum.SOUND;
    }

    @Override
    public void saveMsg(Message msg, SoundMsgDTO body) {
        MessageExtra messageExtra = MessageExtra.builder().soundMsgDTO(Optional.ofNullable(body).orElse(new SoundMsgDTO())).build();
        Message message = new Message();
        BeanUtils.copyProperties(msg, message);
        message.setExtra(messageExtra);
        messageDao.updateById(message);
    }

    @Override
    public Object showMsg(Message message) {
        return message.getExtra().getSoundMsgDTO();
    }
}

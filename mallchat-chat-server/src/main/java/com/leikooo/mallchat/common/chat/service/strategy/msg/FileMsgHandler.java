package com.leikooo.mallchat.common.chat.service.strategy.msg;

import com.leikooo.mallchat.common.chat.domain.entity.Message;
import com.leikooo.mallchat.common.chat.domain.entity.msg.FileMsgDTO;
import com.leikooo.mallchat.common.chat.domain.entity.msg.MessageExtra;
import com.leikooo.mallchat.common.chat.domain.entity.msg.SoundMsgDTO;
import com.leikooo.mallchat.common.chat.domain.enums.MessageTypeEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/3/18
 * @description
 */
@Component
public class FileMsgHandler extends AbstractMsgHandler<FileMsgDTO> {
    @Override
    protected MessageTypeEnum getMessageType() {
        return MessageTypeEnum.FILE;
    }

    @Override
    public void saveMsg(Message msg, FileMsgDTO body) {
        MessageExtra messageExtra = MessageExtra.builder().fileMsg(Optional.ofNullable(body).orElse(new FileMsgDTO())).build();
        Message message = new Message();
        BeanUtils.copyProperties(msg, message);
        message.setExtra(messageExtra);
        messageDao.updateById(message);
    }

    @Override
    public Object showMsg(Message message) {
        return message.getExtra().getFileMsg();
    }

    @Override
    public Object showRelayMsg(Message message) {
        return "图片";
    }
}

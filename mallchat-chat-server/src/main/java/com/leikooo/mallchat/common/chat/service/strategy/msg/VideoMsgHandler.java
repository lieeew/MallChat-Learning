package com.leikooo.mallchat.common.chat.service.strategy.msg;

import com.leikooo.mallchat.common.chat.dao.MessageDao;
import com.leikooo.mallchat.common.chat.domain.entity.Message;
import com.leikooo.mallchat.common.chat.domain.entity.msg.MessageExtra;
import com.leikooo.mallchat.common.chat.domain.entity.msg.VideoMsgDTO;
import com.leikooo.mallchat.common.chat.domain.enums.MessageTypeEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/3/17
 * @description
 */
@Component
public class VideoMsgHandler extends AbstractMsgHandler<VideoMsgDTO> {
    @Resource
    private MessageDao messageDao;

    @Override
    protected MessageTypeEnum getMessageType() {
        return MessageTypeEnum.VIDEO;
    }

    @Override
    public void saveMsg(Message msg, VideoMsgDTO body) {
        Message message = new Message();
        BeanUtils.copyProperties(msg, message);
        message.setExtra(MessageExtra.builder().videoMsgDTO(body).build());
        messageDao.updateById(message);
    }
}

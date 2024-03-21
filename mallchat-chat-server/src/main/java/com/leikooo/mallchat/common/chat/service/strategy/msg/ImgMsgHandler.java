package com.leikooo.mallchat.common.chat.service.strategy.msg;

import com.leikooo.mallchat.common.chat.dao.MessageDao;
import com.leikooo.mallchat.common.chat.domain.entity.Message;
import com.leikooo.mallchat.common.chat.domain.entity.msg.ImgMsgDTO;
import com.leikooo.mallchat.common.chat.domain.entity.msg.MessageExtra;
import com.leikooo.mallchat.common.chat.domain.enums.MessageTypeEnum;
import org.apache.ibatis.transaction.Transaction;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/3/12
 * @description
 */
@Component
public class ImgMsgHandler extends AbstractMsgHandler<ImgMsgDTO> {

    private MessageExtra messageExtra;

    @Override
    protected MessageTypeEnum getMessageType() {
        return MessageTypeEnum.IMG;
    }

    @Override
    public void saveMsg(Message msg, ImgMsgDTO body) {
        messageExtra = MessageExtra.builder().imgMsgDTO(Optional.ofNullable(body).orElse(new ImgMsgDTO())).build();
        Message message = new Message();
        BeanUtils.copyProperties(msg, message);
        message.setExtra(messageExtra);
        messageDao.updateById(message);
    }
}

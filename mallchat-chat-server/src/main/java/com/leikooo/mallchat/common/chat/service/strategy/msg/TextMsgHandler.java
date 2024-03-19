package com.leikooo.mallchat.common.chat.service.strategy.msg;

import com.leikooo.mallchat.common.chat.dao.MessageDao;
import com.leikooo.mallchat.common.chat.domain.entity.Message;
import com.leikooo.mallchat.common.chat.domain.entity.msg.MessageExtra;
import com.leikooo.mallchat.common.chat.domain.enums.MessageTypeEnum;
import com.leikooo.mallchat.common.chat.domain.vo.request.msg.TextMsgReq;
import com.leikooo.mallchat.common.common.utils.AssertUtil;
import com.leikooo.mallchat.common.user.dao.UserDao;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/3/12
 * @description
 */
@Component
public class TextMsgHandler extends AbstractMsgHandler<TextMsgReq> {
    @Resource
    private MessageDao messageDao;

    @Resource
    private UserDao userDao;

    @Override
    protected MessageTypeEnum getMessageType() {
        return MessageTypeEnum.TEXT;
    }

    @Override
    public void check(TextMsgReq textMsgReq, Long roomId, Long uid) {
        Optional.ofNullable(textMsgReq).map(TextMsgReq::getReplyMsgId).ifPresent(replyMsgId -> {
            Message message = messageDao.getById(replyMsgId);
            AssertUtil.isNotEmpty(message, "回复的消息不存在");
        });

        Optional.ofNullable(textMsgReq).map(TextMsgReq::getAtUidList).ifPresent(atUidList -> {
            atUidList.forEach(atUid -> AssertUtil.isTrue(Optional.ofNullable(userDao.getById(atUid)).isPresent(), "艾特的用户不存在"));
        });

        Optional.ofNullable(textMsgReq).map(TextMsgReq::getContent).ifPresent(content -> {
            // todo 检查敏感词
        });
    }

    @Override
    public void saveMsg(Message msg, TextMsgReq body) {
        Message message = new Message();
        BeanUtils.copyProperties(msg, message);
        Optional.ofNullable(body).map(TextMsgReq::getReplyMsgId).ifPresent(message::setReplyMsgId);
        Optional.ofNullable(body).map(TextMsgReq::getContent).ifPresent(message::setContent);
        Optional.ofNullable(body).map(TextMsgReq::getAtUidList).ifPresent(atUidList -> {
            MessageExtra messageExtra = MessageExtra.builder().atUidList(atUidList).build();
            message.setExtra(messageExtra);
        });
        messageDao.updateById(message);
    }
}

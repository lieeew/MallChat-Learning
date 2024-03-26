package com.leikooo.mallchat.common.chat.service.strategy.msg;

import com.leikooo.mallchat.common.chat.dao.MessageDao;
import com.leikooo.mallchat.common.chat.domain.entity.Message;
import com.leikooo.mallchat.common.chat.domain.entity.msg.MessageExtra;
import com.leikooo.mallchat.common.chat.domain.enums.MessageTypeEnum;
import com.leikooo.mallchat.common.chat.domain.vo.request.msg.TextMsgReq;
import com.leikooo.mallchat.common.chat.domain.vo.response.msg.TextMsgResp;
import com.leikooo.mallchat.common.chat.service.cache.MsgCache;
import com.leikooo.mallchat.common.chat.service.factory.MsgHandlerFactory;
import com.leikooo.mallchat.common.common.domain.enums.YesOrNoEnum;
import com.leikooo.mallchat.common.common.utils.AssertUtil;
import com.leikooo.mallchat.common.user.dao.UserDao;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.leikooo.mallchat.common.chat.service.factory.MsgHandlerFactory.msgHandlerMap;

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

    @Resource
    private MsgCache msgCache;

    @Override
    protected MessageTypeEnum getMessageType() {
        return MessageTypeEnum.TEXT;
    }

    @Override
    public void check(TextMsgReq textMsgReq, Long roomId, Long uid) {
        Optional.ofNullable(textMsgReq).map(TextMsgReq::getReplyMsgId).ifPresent(replyMsgId -> {
            Message message = msgCache.getMsg(replyMsgId);
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
        Optional.ofNullable(body).map(TextMsgReq::getContent).ifPresent(message::setContent);
        Optional.ofNullable(body).map(TextMsgReq::getReplyMsgId).ifPresent(replyMsgId -> {
            MessageExtra messageExtra = Optional.ofNullable(message.getExtra()).orElseGet(MessageExtra::new);
            messageExtra.setReplyMsgId(replyMsgId);
            Integer gapCount = messageDao.getGapCount(message.getRoomId(), body.getReplyMsgId(), message.getId());
            messageExtra.setGapCount(gapCount);
            message.setExtra(messageExtra);
        });
        Optional.ofNullable(body).map(TextMsgReq::getAtUidList).ifPresent(atUidList -> {
            MessageExtra messageExtra = MessageExtra.builder().atUidList(atUidList).build();
            message.setExtra(messageExtra);
        });
        messageDao.updateById(message);
    }

    @Override
    public Object showMsg(Message message) {
        TextMsgResp resp = new TextMsgResp();
        MessageExtra extra = message.getExtra();
        List<Long> atList = Optional.ofNullable(extra).map(MessageExtra::getAtUidList).orElse(new ArrayList<>());
        Long replyMsgId = Optional.ofNullable(extra).map(MessageExtra::getReplyMsgId).orElse(null);
        if (Objects.nonNull(replyMsgId)) {
            Message replyMsg = msgCache.getMsg(replyMsgId);
            resp.setContent(message.getContent());
            resp.setReply(buildReply(message, replyMsgId, replyMsg));
        }
        resp.setAtUidList(atList);
        return resp;
    }

    private TextMsgResp.ReplyMsg buildReply(Message message, Long replyMsgId, Message replyMsg) {
        return TextMsgResp.ReplyMsg.builder()
                .type(MessageTypeEnum.TEXT.getType())
                .canCallback(YesOrNoEnum.YES.getStatus())
                .uid(replyMsg.getFromUid())
                .id(replyMsgId)
                .gapCount(message.getExtra().getGapCount())
                .body(MsgHandlerFactory.getStrategyNoNull(replyMsg.getType()).showRelayMsg(replyMsg))
                .build();
    }

    @Override
    public Object showRelayMsg(Message message) {
        return message.getContent();
    }
}

package com.leikooo.mallchat.common.chat.service.strategy.mark;

import com.leikooo.mallchat.common.chat.dao.MessageDao;
import com.leikooo.mallchat.common.chat.dao.MessageMarkDao;
import com.leikooo.mallchat.common.chat.domain.dto.ChatMessageMarkDTO;
import com.leikooo.mallchat.common.chat.domain.entity.Message;
import com.leikooo.mallchat.common.chat.domain.entity.MessageMark;
import com.leikooo.mallchat.common.chat.domain.enums.MessageMarkActTypeEnum;
import com.leikooo.mallchat.common.chat.domain.enums.MessageMarkTypeEnum;
import com.leikooo.mallchat.common.chat.service.factory.MarkMsgFactory;
import com.leikooo.mallchat.common.common.domain.enums.YesOrNoEnum;
import com.leikooo.mallchat.common.common.event.MessageMarkEvent;
import com.leikooo.mallchat.common.common.exception.BusinessException;
import com.leikooo.mallchat.common.common.utils.AssertUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/4/6
 * @description
 */
@Component
public abstract class AbstractMsgMarkStrategy implements MsgMarkStrategy {
    @Resource
    private MessageMarkDao messageMarkDao;

    @Resource
    private MessageDao messageDao;

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    public abstract MessageMarkTypeEnum getMessageMarkEnum();

    @PostConstruct
    private void init() {
        MarkMsgFactory.msgMarkStrategyMap.put(getMessageMarkEnum().getType(), this);
    }

    @Override
    @Transactional
    public void doMark(Long messageId, Long uid) {
        exec(MessageAction.builder().messageId(messageId).userId(uid).actionType(MessageMarkActTypeEnum.MARK).build());
    }

    @Override
    @Transactional
    public void unMark(Long messageId, Long uid) {
        exec(MessageAction.builder().messageId(messageId).userId(uid).actionType(MessageMarkActTypeEnum.UN_MARK).build());
    }

    @Override
    @Transactional
    public void execute(Long messageId, Long uid, Integer actType) {
        if (Objects.equals(actType, MessageMarkActTypeEnum.MARK.getType())) {
            this.doMark(messageId, uid);
        } else {
            this.unMark(messageId, uid);
        }
    }

    private void exec(MessageAction messageAction) {
        Long messageId = messageAction.getMessageId();
        Long uid = messageAction.getUserId();
        checkMessage(messageId, uid);
        // 查询是否进行了标记，如果标记了就需要取消
        Integer type = getMessageMarkEnum().getType();
        MessageMark oldMark = messageMarkDao.getOneMark(messageId, uid, type);
        if (Objects.isNull(oldMark) && Objects.equals(messageAction.getActionType(), MessageMarkActTypeEnum.UN_MARK)) {
            // 取消的类型，数据库一定有记录，没有就直接跳过操作
            return;
        }
        if (messageMarkDao.saveOrUpdate(buildUpdateMessageMark(messageAction, oldMark))) {
            ChatMessageMarkDTO chatMessageMarkDTO = new ChatMessageMarkDTO(messageAction.getUserId(), messageAction.getMessageId(), getMessageMarkEnum().getType(), messageAction.getActionType().getType());
            applicationEventPublisher.publishEvent(new MessageMarkEvent(this, chatMessageMarkDTO));
        }
    }

    private MessageMark buildUpdateMessageMark(MessageAction messageAction, MessageMark oldMark) {
        return MessageMark.builder()
                .id(Optional.ofNullable(oldMark).map(MessageMark::getId).orElse(null))
                .msgId(messageAction.getMessageId())
                .type(getMessageMarkEnum().getType())
                .status(transferAct(messageAction.getActionType()))
                .uid(messageAction.getUserId()).build();
    }

    private Integer transferAct(MessageMarkActTypeEnum actTypeEnum) {
        if (Objects.equals(actTypeEnum, MessageMarkActTypeEnum.MARK)) {
            return YesOrNoEnum.NO.getStatus();
        }
        if (Objects.equals(actTypeEnum, MessageMarkActTypeEnum.UN_MARK)) {
            return YesOrNoEnum.YES.getStatus();
        }
        throw new BusinessException("动作类型 1确认 2取消");
    }

    private void checkMessage(Long messageId, Long uid) {
        Message message = messageDao.getById(messageId);
        AssertUtil.isNotEmpty(message, "消息不能为空");
        AssertUtil.notEqual(message.getFromUid(), uid, "抱歉不能操作自己发送的消息~");
    }
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class MessageAction {

    private Long messageId;

    private Long userId;

    private MessageMarkActTypeEnum actionType;

}
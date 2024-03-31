package com.leikooo.mallchat.common.chat.service.strategy.msg;

import cn.hutool.core.collection.CollectionUtil;
import com.leikooo.mallchat.common.chat.dao.MessageDao;
import com.leikooo.mallchat.common.chat.domain.entity.Message;
import com.leikooo.mallchat.common.chat.domain.entity.msg.MessageExtra;
import com.leikooo.mallchat.common.chat.domain.entity.msg.TextMsgDTO;
import com.leikooo.mallchat.common.chat.domain.enums.MessageTypeEnum;
import com.leikooo.mallchat.common.chat.domain.vo.response.msg.TextMsgResp;
import com.leikooo.mallchat.common.chat.service.cache.MsgCache;
import com.leikooo.mallchat.common.chat.service.factory.MsgHandlerFactory;
import com.leikooo.mallchat.common.common.domain.enums.YesOrNoEnum;
import com.leikooo.mallchat.common.common.utils.AssertUtil;
import com.leikooo.mallchat.common.user.dao.UserDao;
import com.leikooo.mallchat.common.user.domain.entity.User;
import com.leikooo.mallchat.common.user.domain.enums.UserRoleEnum;
import com.leikooo.mallchat.common.user.service.RoleService;
import com.leikooo.mallchat.common.user.service.cache.UserCache;
import com.leikooo.mallchat.common.user.service.cache.UserInfoCache;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/3/12
 * @description
 */
@Component
public class TextMsgHandler extends AbstractMsgHandler<TextMsgDTO> {
    public static final long AT_ALL_SIGN = 0L;

    @Resource
    private MessageDao messageDao;

    @Resource
    private UserDao userDao;

    @Resource
    private MsgCache msgCache;

    @Resource
    private UserInfoCache userInfoCache;

    @Resource
    private RoleService roleService;

    @Override
    protected MessageTypeEnum getMessageType() {
        return MessageTypeEnum.TEXT;
    }

    @Override
    public void check(TextMsgDTO textMsgReq, Long roomId, Long uid) {
        checkReplyMsg(textMsgReq);
        checkAtUser(textMsgReq, uid);
        checkSensitiveWords(textMsgReq);
    }

    private void checkSensitiveWords(TextMsgDTO textMsgReq) {
        Optional.ofNullable(textMsgReq).map(TextMsgDTO::getContent).ifPresent(content -> {
            // todo 检查敏感词
        });
    }

    private void checkReplyMsg(TextMsgDTO textMsgReq) {
        Optional.ofNullable(textMsgReq).map(TextMsgDTO::getReplyMsgId).ifPresent(replyMsgId -> {
            Message message = msgCache.getMsg(replyMsgId);
            AssertUtil.isNotEmpty(message, "回复的消息不存在");
        });
    }

    @Override
    public void saveMsg(Message msg, TextMsgDTO body) {
        Message message = new Message();
        BeanUtils.copyProperties(msg, message);
        Optional.ofNullable(body).map(TextMsgDTO::getContent).ifPresent(message::setContent);
        saveReplayMsg(body, message);
        saveAtMsg(body, message);
        messageDao.updateById(message);
    }

    public void checkAtUser(TextMsgDTO textMsgReq, Long uid) {
        List<Long> atUserIds = Optional.ofNullable(textMsgReq).map(TextMsgDTO::getAtUidList)
                .filter(CollectionUtil::isNotEmpty)
                .orElse(new ArrayList<>())
                .stream().distinct().collect(Collectors.toList());
        if (CollectionUtil.isEmpty(atUserIds)) {
            return;
        }
        Map<Long, User> userInfoBatch = userInfoCache.getBatch(atUserIds);
        long effectiveCount = userInfoBatch.values().stream().filter(Objects::nonNull).count();
        AssertUtil.equal(effectiveCount, userInfoBatch.size(), "@的用户不存在");
        if (isAtAllUser(atUserIds)) {
            // 判断是否有权限
            AssertUtil.isTrue(roleService.hasPower(uid, UserRoleEnum.CHAT_MANAGER), "没有权限艾特所有人");
        }
    }

    private void saveAtMsg(TextMsgDTO body, Message message) {
        Optional.ofNullable(body).map(TextMsgDTO::getAtUidList).ifPresent(atUidList -> {
            MessageExtra messageExtra = MessageExtra.builder().atUidList(atUidList).build();
            message.setExtra(messageExtra);
        });
    }

    private boolean isAtAllUser(List<Long> atUserIds) {
        if (CollectionUtil.isEmpty(atUserIds)) {
            return false;
        }
        return atUserIds.contains(AT_ALL_SIGN);
    }

    private void saveReplayMsg(TextMsgDTO body, Message message) {
        Optional.ofNullable(body).map(TextMsgDTO::getReplyMsgId).ifPresent(replyMsgId -> {
            MessageExtra messageExtra = Optional.ofNullable(message.getExtra()).orElseGet(MessageExtra::new);
            messageExtra.setReplyMsgId(replyMsgId);
            Integer gapCount = messageDao.getGapCount(message.getRoomId(), body.getReplyMsgId(), message.getId());
            messageExtra.setGapCount(gapCount);
            message.setExtra(messageExtra);
        });
    }

    @Override
    public Object showMsg(Message message) {
        TextMsgResp resp = new TextMsgResp();
        MessageExtra extra = message.getExtra();
        resp.setAtUidList(Optional.ofNullable(extra).map(MessageExtra::getAtUidList).orElse(new ArrayList<>()));
        setReplyMsgIfPresent(message, resp, extra);
        return resp;
    }

    private void setReplyMsgIfPresent(Message message, TextMsgResp resp, MessageExtra extra) {
        Long replyMsgId = Optional.ofNullable(extra).map(MessageExtra::getReplyMsgId).orElse(null);
        if (Objects.isNull(replyMsgId)) {
            return;
        }
        Message replyMsg = msgCache.getMsg(replyMsgId);
        resp.setContent(message.getContent());
        resp.setReply(buildReply(message, replyMsgId, replyMsg));
    }

    private TextMsgResp.ReplyMsg buildReply(Message message, Long replyMsgId, Message replyMsg) {
        return TextMsgResp.ReplyMsg.builder()
                .type(MessageTypeEnum.TEXT.getType())
                .canCallback(YesOrNoEnum.YES.getStatus())
                .uid(replyMsg.getFromUid())
                .id(replyMsgId)
                .username(Optional.ofNullable(userDao.getById(replyMsgId)).map(User::getName).orElse(""))
                .gapCount(message.getExtra().getGapCount())
                .body(MsgHandlerFactory.getStrategyNoNull(replyMsg.getType()).showRelayMsg(replyMsg))
                .build();
    }

    @Override
    public Object showRelayMsg(Message message) {
        return message.getContent();
    }
}

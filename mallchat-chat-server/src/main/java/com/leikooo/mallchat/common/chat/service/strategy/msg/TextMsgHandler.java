package com.leikooo.mallchat.common.chat.service.strategy.msg;

import cn.hutool.core.collection.CollectionUtil;
import com.leikooo.mallchat.common.chat.adaptor.MessageAdapter;
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
import com.leikooo.mallchat.common.common.utils.discover.AbstractUrlDiscover;
import com.leikooo.mallchat.common.common.utils.discover.PrioritizedUrlDiscover;
import com.leikooo.mallchat.common.common.utils.discover.domain.UrlInfo;
import com.leikooo.mallchat.common.common.utils.sensitive.SensitiveWordBs;
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

    @Resource
    private SensitiveWordBs sensitiveWordBs;

    private final AbstractUrlDiscover URL_TITLE_DISCOVER = new PrioritizedUrlDiscover();

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
    public void saveMsg(final Message msg, TextMsgDTO body) {
        Message message = new Message();
        BeanUtils.copyProperties(msg, message);
        message.setExtra(Optional.of(message).map(Message::getExtra).orElseGet(MessageExtra::new));
        saveContentAndResolveUrlIfExist(message, body);
        saveReplayMsg(body, message);
        saveAtMsg(body, message);
        messageDao.updateById(message);
    }

    private void saveContentAndResolveUrlIfExist(Message msg, TextMsgDTO body) {
        Optional.ofNullable(body).map(TextMsgDTO::getContent).ifPresent((content) -> {
            msg.setContent(sensitiveWordBs.filter(content));
            saveUrl(content, msg);
        });
    }

    private void saveUrl(String content, Message message) {
        Map<String, UrlInfo> urlContent = URL_TITLE_DISCOVER.getUrlContent(content);
        message.getExtra().setUrlContentMap(urlContent);
    }

    public void checkAtUser(TextMsgDTO textMsgReq, Long uid) {
        List<Long> atUserIds = Optional.ofNullable(textMsgReq).map(TextMsgDTO::getAtUidList).filter(CollectionUtil::isNotEmpty).orElse(new ArrayList<>())
                .stream().distinct().collect(Collectors.toList());
        if (CollectionUtil.isEmpty(atUserIds)) {
            return;
        }
        if (isAtAllUser(atUserIds)) {
            checkAtUserRole(uid);
            return;
        }
        checkAtUser(atUserIds);
    }

    private void checkAtUserRole(Long uid) {
        // 判断是否有权限
        AssertUtil.isTrue(roleService.hasPower(uid, UserRoleEnum.CHAT_MANAGER), "没有权限艾特所有人");
    }

    private void checkAtUser(List<Long> atUserIds) {
        Map<Long, User> userInfoBatch = userInfoCache.getBatch(atUserIds);
        long effectiveCount = userInfoBatch.values().stream().filter(Objects::nonNull).count();
        // 这里需要把第二个参数强转成 long 类型，这样比较 Objects.equals 才不会报错
        AssertUtil.equal(effectiveCount, (long) userInfoBatch.size(), "@的用户不存在");
    }

    private void saveAtMsg(TextMsgDTO body, Message message) {
        Optional.ofNullable(body).map(TextMsgDTO::getAtUidList)
                .ifPresent(atUidList -> message.getExtra().setAtUidList(atUidList));
    }

    private boolean isAtAllUser(List<Long> atUserIds) {
        if (CollectionUtil.isEmpty(atUserIds)) {
            return false;
        }
        return atUserIds.contains(AT_ALL_SIGN);
    }

    private void saveReplayMsg(TextMsgDTO body, Message message) {
        Optional.ofNullable(body).map(TextMsgDTO::getReplyMsgId).ifPresent(replyMsgId -> {
            MessageExtra messageExtra = message.getExtra();
            messageExtra.setReplyMsgId(replyMsgId);
            Integer gapCount = messageDao.getGapCount(message.getRoomId(), body.getReplyMsgId(), message.getId());
            messageExtra.setGapCount(gapCount);
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
        resp.setReply(getReply(message, replyMsg));
    }

    private TextMsgResp.ReplyMsg getReply(Message message, Message replyMsg) {
        String userName = userDao.getById(replyMsg.getFromUid()).getName();
        return MessageAdapter.buildTextReplyMsg(message, replyMsg, userName);
    }

    @Override
    public Object showRelayMsg(Message message) {
        return message.getContent();
    }
}

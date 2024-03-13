package com.leikooo.mallchat.common.chat.service.strategy.msg;

import cn.hutool.core.bean.BeanUtil;
import com.leikooo.mallchat.common.chat.dao.MessageDao;
import com.leikooo.mallchat.common.chat.domain.entity.Message;
import com.leikooo.mallchat.common.chat.domain.enums.MessageStatusEnum;
import com.leikooo.mallchat.common.chat.domain.enums.MessageTypeEnum;
import com.leikooo.mallchat.common.chat.domain.vo.request.ChatMessageReq;
import com.leikooo.mallchat.common.chat.service.factory.MsgHandlerFactory;
import com.leikooo.mallchat.common.common.utils.AssertUtil;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.reflect.ParameterizedType;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/3/11
 * @description
 */
@Component
public abstract class AbstractMsgHandler<Req> {
    @Resource
    private MessageDao messageDao;

    private Class<Req> bodyClass;

    @PostConstruct
    private void init() {
        ParameterizedType genericSuperclass = (ParameterizedType) this.getClass().getGenericSuperclass();
        bodyClass = (Class<Req>) genericSuperclass.getActualTypeArguments()[0];
        MsgHandlerFactory.register(getMessageType().getType(), this);
    }

    protected abstract MessageTypeEnum getMessageType();

    public void check(Req req, Long roomId, Long uid) {

    };

    private Long checkAndSave(ChatMessageReq req, Long uid) {
        Req parseBody = parseBody(req.getBody());
        AssertUtil.allCheckValidate(parseBody);
        check(parseBody, req.getRoomId(), uid);
        Message message = Message.builder().fromUid(uid).roomId(req.getRoomId()).status(MessageStatusEnum.of(MessageStatusEnum.NORMAL.getStatus()).getStatus()).build();
        messageDao.save(message);
        // 子类扩展
        saveMsg(message, parseBody);
        return message.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    public Long checkAndSaveMsg(ChatMessageReq req, Long uid) {
        return ((AbstractMsgHandler<?>) AopContext.currentProxy()).checkAndSave(req, uid);
    }

    public abstract void saveMsg(Message msg, Req body);

    private Req parseBody(Object body) {
        if (bodyClass.isAssignableFrom(body.getClass())) {
            return (Req) body;
        }
        return BeanUtil.toBean(body, bodyClass);
    }
}

package com.leikooo.mallchat.common.chat.service.strategy.msg;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.leikooo.mallchat.common.chat.dao.MessageDao;
import com.leikooo.mallchat.common.chat.domain.entity.Message;
import com.leikooo.mallchat.common.chat.domain.enums.MessageStatusEnum;
import com.leikooo.mallchat.common.chat.domain.enums.MessageTypeEnum;
import com.leikooo.mallchat.common.chat.domain.vo.request.ChatMessageReq;
import com.leikooo.mallchat.common.chat.service.factory.MsgHandlerFactory;
import com.leikooo.mallchat.common.common.utils.AssertUtil;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.reflect.ParameterizedType;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/3/11
 * @description
 */
public abstract class AbstractMsgHandler<Req> {
    @Resource
    protected MessageDao messageDao;

    private Class<Req> bodyClass;

    protected Object extraMessage;

    @PostConstruct
    private void init() {
        ParameterizedType genericSuperclass = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.bodyClass = (Class<Req>) genericSuperclass.getActualTypeArguments()[0];
        MsgHandlerFactory.register(getMessageType().getType(), this);
    }

    protected abstract MessageTypeEnum getMessageType();

    public void check(Req req, Long roomId, Long uid) {

    }

    @Transactional(rollbackFor = Exception.class)
    public Long checkAndSave(ChatMessageReq req, Long uid) {
//        这个可以获取事务的详细信息
//        if (TransactionSynchronizationManager.isActualTransactionActive()) {
//            String currentTransactionName = TransactionSynchronizationManager.getCurrentTransactionName();
//            TransactionStatus transactionStatus = TransactionAspectSupport.currentTransactionStatus();
//        }
        Req parseBody = this.parseBody(req.getBody());
        AssertUtil.allCheckValidate(parseBody);
        check(parseBody, req.getRoomId(), uid);
        Message message = Message.builder().fromUid(uid).type(req.getMsgType()).roomId(req.getRoomId()).status(MessageStatusEnum.of(MessageStatusEnum.NORMAL.getStatus()).getStatus()).build();
        extraMessage = parseBody;
        messageDao.save(message);
        // 子类扩展
        saveMsg(message, parseBody);
        return message.getId();
    }

    public abstract void saveMsg(Message msg, Req body);

    private Req parseBody(Object body) {
        if (bodyClass.isAssignableFrom(body.getClass())) {
            return (Req) body;
        }
        return BeanUtil.toBean(body, bodyClass);
    }

    public Object showMsg() {
        return extraMessage;
    }
}

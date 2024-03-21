package com.leikooo.mallchat.common.user.consumer;

import com.leikooo.mallchat.common.common.constant.MQConstant;
import com.leikooo.mallchat.common.common.domain.dto.PushMessageDTO;
import com.leikooo.mallchat.common.user.domain.enums.WSPushTypeEnum;
import com.leikooo.mallchat.common.user.service.WebSocketService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;

import javax.annotation.Resource;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/3/20
 * @description
 */
@Slf4j
@RocketMQMessageListener(topic = MQConstant.PUSH_TOPIC, consumerGroup = MQConstant.PUSH_GROUP)
public class PushConsumer implements RocketMQListener<PushMessageDTO> {
    @Resource
    private WebSocketService webSocketService;

    @Override
    public void onMessage(PushMessageDTO message) {
        switch (WSPushTypeEnum.of(message.getPushType())) {
            case ALL:
                // 推送给所有用户
                webSocketService.sendToAllOnline(message.getWsBaseMsg());
                break;
            case USER:
                webSocketService.sendToUidList(message.getWsBaseMsg(), message.getUidList());
                break;
            default:
                log.error("PushConsumer 消费到未知的推送类型: {}", message.getPushType());
        }
    }
}

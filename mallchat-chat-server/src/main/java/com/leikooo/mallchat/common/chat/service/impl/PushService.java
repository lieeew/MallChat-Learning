package com.leikooo.mallchat.common.chat.service.impl;

import com.leikooo.mallchat.common.common.constant.MQConstant;
import com.leikooo.mallchat.common.common.domain.dto.PushMessageDTO;
import com.leikooo.mallchat.transaction.service.MqProducer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/3/19
 * @description
 */
@Service
public class PushService {
    @Resource
    private MqProducer mqProducer;

    public void pushMsg(PushMessageDTO pushMessageDTO) {
        mqProducer.sendMsg(MQConstant.PUSH_TOPIC, pushMessageDTO);
    }
}

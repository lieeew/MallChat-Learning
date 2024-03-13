package com.leikooo.mallchat.common.chat.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leikooo.mallchat.common.chat.domain.entity.Message;
import com.leikooo.mallchat.common.chat.mapper.MessageMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 消息表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @since 2024-03-02
 */
@Service
public class MessageDao extends ServiceImpl<MessageMapper, Message> {

}

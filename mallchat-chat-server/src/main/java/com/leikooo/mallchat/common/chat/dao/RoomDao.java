package com.leikooo.mallchat.common.chat.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leikooo.mallchat.common.chat.domain.entity.Room;
import com.leikooo.mallchat.common.chat.mapper.RoomMapper;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 房间表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @since 2024-03-02
 */
@Service
public class RoomDao extends ServiceImpl<RoomMapper, Room> {

    public void refreshActiveTime(Long roomId, Date createTime, Long msgId) {
        lambdaUpdate()
                // todo 消息消费顺序可能会导致消息不是按照顺序消费
                .eq(Room::getId, roomId)
                .set(Room::getUpdateTime, createTime)
                .set(Room::getLastMsgId, msgId)
                .update();
    }
}

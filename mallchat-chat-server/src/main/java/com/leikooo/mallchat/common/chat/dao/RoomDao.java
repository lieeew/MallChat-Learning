package com.leikooo.mallchat.common.chat.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leikooo.mallchat.common.chat.domain.entity.Room;
import com.leikooo.mallchat.common.chat.mapper.RoomMapper;
import org.springframework.stereotype.Service;

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

}

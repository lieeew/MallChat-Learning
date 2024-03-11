package com.leikooo.mallchat.common.chat.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leikooo.mallchat.common.chat.mapper.RoomFriendMapper;
import com.leikooo.mallchat.common.common.domain.enums.YesOrNoEnum;
import com.leikooo.mallchat.common.user.domain.entity.RoomFriend;
import com.leikooo.mallchat.common.user.domain.enums.RoomStatusEnum;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 单聊房间表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @since 2024-03-02
 */
@Service
public class RoomFriendDao extends ServiceImpl<RoomFriendMapper, RoomFriend> {

    public void restoreFriendRoom(String roomKey) {
        lambdaUpdate()
                .eq(RoomFriend::getRoomKey, roomKey)
                .set(RoomFriend::getStatus, YesOrNoEnum.NO.getStatus())
                .update();
    }

    public void deleteFriendRoom(String roomKey) {
        lambdaUpdate()
                .eq(RoomFriend::getRoomKey, roomKey)
                .set(RoomFriend::getStatus, RoomStatusEnum.DISABLE.getStatus())
                .update();
    }
}

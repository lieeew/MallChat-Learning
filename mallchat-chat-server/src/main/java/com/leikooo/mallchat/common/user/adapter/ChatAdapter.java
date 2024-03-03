package com.leikooo.mallchat.common.user.adapter;

import com.leikooo.mallchat.common.common.domain.enums.YesOrNoEnum;
import com.leikooo.mallchat.common.user.domain.entity.Room;
import com.leikooo.mallchat.common.user.domain.entity.RoomFriend;

import java.util.List;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/3/2
 * @description
 */
public class ChatAdapter {
    public static RoomFriend buildFriendRoom(Long roomId, String roomKey, List<Long> uidList) {
        return RoomFriend.builder()
                .roomId(roomId)
                .uid1(uidList.get(0))
                .uid2(uidList.get(1))
                .roomKey(roomKey)
                .build();
    }

    public static Room buildRoom(Integer type) {
        return Room.builder()
                .type(type)
                .hotFlag(YesOrNoEnum.NO.getStatus())
                .build();
    }
}

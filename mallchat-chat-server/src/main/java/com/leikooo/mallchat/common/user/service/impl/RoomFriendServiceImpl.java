package com.leikooo.mallchat.common.user.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.leikooo.mallchat.common.user.adapter.FriendAdapter;
import com.leikooo.mallchat.common.user.adapter.ChatAdapter;
import com.leikooo.mallchat.common.user.dao.RoomDao;
import com.leikooo.mallchat.common.user.dao.RoomFriendDao;
import com.leikooo.mallchat.common.user.domain.entity.GenerateRoomKeyResult;
import com.leikooo.mallchat.common.user.domain.entity.Room;
import com.leikooo.mallchat.common.user.domain.entity.RoomFriend;
import com.leikooo.mallchat.common.user.domain.enums.RoomTypeEnum;
import com.leikooo.mallchat.common.user.service.RoomFriendService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/3/2
 * @description
 */
@Service
public class RoomFriendServiceImpl implements RoomFriendService {
    @Resource
    private RoomFriendDao roomFriendDao;

    @Resource
    private RoomDao roomDao;

    @Override
    public void creatFriendRoom(Long uid, Long applyId) {
        GenerateRoomKeyResult generateRoomKeyResult = FriendAdapter.generateRoomKey(uid, applyId);
        RoomFriend roomFriend = roomFriendDao.getById(generateRoomKeyResult.getRoomKey());
        if (ObjectUtil.isNotNull(roomFriend)) {
            roomFriendDao.restoreFriendRoom(generateRoomKeyResult.getRoomKey());
        } else {
            Room room = creatRoom(RoomTypeEnum.of(RoomTypeEnum.FRIEND.getType()));
            roomFriend = ChatAdapter.buildFriendRoom(room.getId(), generateRoomKeyResult.getRoomKey(), generateRoomKeyResult.getUidList());
            roomFriendDao.save(roomFriend);
        }
    }

    private Room creatRoom(RoomTypeEnum typeEnum) {
        Room insert = ChatAdapter.buildRoom(typeEnum.getType());
        roomDao.save(insert);
        return insert;
    }
}

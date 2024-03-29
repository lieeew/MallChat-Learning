package com.leikooo.mallchat.common.chat.service;

import com.leikooo.mallchat.common.chat.domain.entity.RoomFriend;

import java.util.List;

/**
 * <p>
 * 单聊房间表 服务类
 * </p>
 *
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @since 2024-03-02
 */
public interface RoomFriendService {

    RoomFriend creatFriendRoom(Long uid, Long applyId);

    void deleteFriendRoom(List<Long> uidList);
}

package com.leikooo.mallchat.common.user.adapter;

import cn.hutool.core.collection.CollectionUtil;
import com.leikooo.mallchat.common.common.utils.AssertUtil;
import com.leikooo.mallchat.common.user.domain.entity.GenerateRoomKeyResult;
import com.leikooo.mallchat.common.user.domain.entity.User;
import com.leikooo.mallchat.common.user.domain.entity.UserApply;
import com.leikooo.mallchat.common.user.domain.entity.UserFriend;
import com.leikooo.mallchat.common.user.domain.vo.response.friend.FriendApplyResp;
import com.leikooo.mallchat.common.user.domain.vo.response.friend.FriendResp;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/2/26
 * @description
 */
public class FriendAdapter {
    public static List<FriendResp> buildFriendResp(List<UserFriend> list, List<User> userList) {
        Map<Long, User> userMap = userList.stream().collect(Collectors.toMap(User::getId, user -> user));
        return list.stream()
                .map(u -> {
                    FriendResp friendResp = new FriendResp();
                    friendResp.setUid(u.getFriendUid());
                    User user = userMap.get(u.getFriendUid());
                    if (Objects.nonNull(user)) {
                        friendResp.setActiveStatus(user.getStatus());
                    }
                    return friendResp;
                })
                .collect(Collectors.toList());
    }

    public static List<FriendApplyResp> buildFriendApplyPageResp(List<UserApply> records) {
        return records.stream().map(p -> FriendApplyResp.builder()
                .uid(p.getUid())
                .msg(p.getMsg())
                .status(p.getStatus())
                .type(p.getType())
                .build()).collect(Collectors.toList());
    }

    public static GenerateRoomKeyResult generateRoomKey(Long uid, Long applyId) {
        List<Long> collect = Stream.of(uid, applyId).sorted(Comparator.comparing(Long::shortValue)).collect(Collectors.toList());
        return GenerateRoomKeyResult.builder()
                .roomKey(collect.get(0) + "_" + collect.get(1))
                .uidList(collect)
                .build();
    }
}

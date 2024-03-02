package com.leikooo.mallchat.common.user.adapter;

import com.leikooo.mallchat.common.user.domain.entity.User;
import com.leikooo.mallchat.common.user.domain.entity.UserApply;
import com.leikooo.mallchat.common.user.domain.entity.UserFriend;
import com.leikooo.mallchat.common.user.domain.vo.response.friend.FriendApplyResp;
import com.leikooo.mallchat.common.user.domain.vo.response.friend.FriendResp;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
}

package com.leikooo.mallchat.common.user.service.impl;

import com.leikooo.mallchat.common.common.domain.vo.request.CursorPageBaseReq;
import com.leikooo.mallchat.common.common.domain.vo.response.ApiResult;
import com.leikooo.mallchat.common.common.domain.vo.response.CursorPageBaseResp;
import com.leikooo.mallchat.common.user.adapter.FriendAdapter;
import com.leikooo.mallchat.common.user.dao.UserDao;
import com.leikooo.mallchat.common.user.dao.UserFriendDao;
import com.leikooo.mallchat.common.user.domain.entity.User;
import com.leikooo.mallchat.common.user.domain.entity.UserFriend;
import com.leikooo.mallchat.common.user.domain.vo.request.friend.FriendCheckReq;
import com.leikooo.mallchat.common.user.domain.vo.response.friend.FriendCheckResp;
import com.leikooo.mallchat.common.user.domain.vo.response.friend.FriendResp;
import com.leikooo.mallchat.common.user.service.UserFriendService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/2/26
 * @description
 */
@Service
public class UserFriendServiceImpl implements UserFriendService {
    @Resource
    private UserFriendDao userFriendDao;

    @Resource
    private UserDao userDao;

    @Override
    public CursorPageBaseResp<FriendResp> getFriendList(Long uid, CursorPageBaseReq req) {
        CursorPageBaseResp<UserFriend> userFriendList = userFriendDao.getUserFriendList(uid, req);
        List<Long> friendUidList = userFriendList.getList().stream().map(UserFriend::getFriendUid).collect(Collectors.toList());
        List<User> userStatusList = friendUidList.stream().map(friendUid -> User.builder().id(friendUid).status(userDao.getUserStatus(friendUid)).build()).collect(Collectors.toList());
        return CursorPageBaseResp.init(userFriendList, FriendAdapter.buildFriendResp(userFriendList.getList(), userStatusList));
    }

    @Override
    public FriendCheckResp checkUserIsFriend(Long uid, FriendCheckReq req) {
        List<UserFriend> myFriendList = userFriendDao.getMyFriendList(uid, req.getUidList());
        Set<Long> myFriendUidSet = myFriendList.stream().map(UserFriend::getFriendUid).collect(Collectors.toSet());
        List<FriendCheckResp.FriendCheck> friendCheckList = req.getUidList().stream().map(friendId ->
                FriendCheckResp.FriendCheck.builder().uid(friendId).isFriend(myFriendUidSet.contains(friendId)).build()
        ).collect(Collectors.toList());
        return new FriendCheckResp(friendCheckList);
    }
}

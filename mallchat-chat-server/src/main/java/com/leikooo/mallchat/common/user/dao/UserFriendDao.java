package com.leikooo.mallchat.common.user.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leikooo.mallchat.common.common.domain.enums.YesOrNoEnum;
import com.leikooo.mallchat.common.common.domain.vo.request.CursorPageBaseReq;
import com.leikooo.mallchat.common.common.domain.vo.response.CursorPageBaseResp;
import com.leikooo.mallchat.common.common.utils.CursorUtils;
import com.leikooo.mallchat.common.user.domain.entity.UserFriend;
import com.leikooo.mallchat.common.user.mapper.UserFriendMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户联系人表 服务实现类
 *
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @since 2024-02-25
 */
@Service
public class UserFriendDao extends ServiceImpl<UserFriendMapper, UserFriend> {
    public CursorPageBaseResp<UserFriend> getUserFriendList(Long uid, CursorPageBaseReq req) {
        return CursorUtils.getCursorPageByMysql(this, req, wrapper -> {
            wrapper.eq(UserFriend::getUid, uid);
        }, UserFriend::getId);
    }

    public List<UserFriend> getMyFriendList(Long uid, List<Long> uidList) {
        return lambdaQuery()
                .select(UserFriend::getUid, UserFriend::getFriendUid)
                .eq(UserFriend::getUid, uid)
                .in(UserFriend::getFriendUid, uidList)
                .list();
    }

    /**
     * @param uid
     * @param targetUid
     * @return true 这个用户存在
     */
    public boolean isHaveUserFriend(Long uid, Long targetUid) {
        return lambdaQuery()
                .select(UserFriend::getUid, UserFriend::getUid)
                .eq(UserFriend::getUid, uid)
                .eq(UserFriend::getFriendUid, targetUid)
                .nonEmptyOfEntity();
    }

    /**
     * 获取我的某一个好友
     *
     * @param uid
     * @param targetUid
     * @return
     */
    public UserFriend getMyFriend(Long uid, Long targetUid) {
        return lambdaQuery()
                .eq(UserFriend::getUid, uid)
                .eq(UserFriend::getFriendUid, targetUid)
                .one();
    }

    public boolean deleteUserAllFriends(Long uid) {
        return lambdaUpdate()
                .set(UserFriend::getDeleteStatus, YesOrNoEnum.YES.getStatus())
                .in(
                        UserFriend::getFriendUid,
                        lambdaQuery()
                                .select(UserFriend::getFriendUid)
                                .eq(UserFriend::getUid, uid)
                                .list()
                )
                .update();
    }

    public void deleteFriend(Long uid, Long targetUid) {
        lambdaUpdate()
                .set(UserFriend::getDeleteStatus, YesOrNoEnum.YES.getStatus())
                .eq(UserFriend::getUid, uid)
                .eq(UserFriend::getFriendUid, targetUid)
                .update();
    }
}

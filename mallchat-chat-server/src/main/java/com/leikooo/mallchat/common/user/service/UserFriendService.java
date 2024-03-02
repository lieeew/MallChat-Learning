package com.leikooo.mallchat.common.user.service;

import com.leikooo.mallchat.common.common.domain.vo.request.CursorPageBaseReq;
import com.leikooo.mallchat.common.common.domain.vo.request.PageBaseReq;
import com.leikooo.mallchat.common.common.domain.vo.response.CursorPageBaseResp;
import com.leikooo.mallchat.common.common.domain.vo.response.PageBaseResp;
import com.leikooo.mallchat.common.user.domain.vo.request.friend.FriendApplyReq;
import com.leikooo.mallchat.common.user.domain.vo.request.friend.FriendApproveReq;
import com.leikooo.mallchat.common.user.domain.vo.request.friend.FriendCheckReq;
import com.leikooo.mallchat.common.user.domain.vo.request.friend.FriendDeleteReq;
import com.leikooo.mallchat.common.user.domain.vo.response.friend.FriendApplyResp;
import com.leikooo.mallchat.common.user.domain.vo.response.friend.FriendCheckResp;
import com.leikooo.mallchat.common.user.domain.vo.response.friend.FriendResp;
import com.leikooo.mallchat.common.user.domain.vo.response.friend.FriendUnreadResp;

/**
 * <p>
 * 用户联系人表 服务类
 * </p>
 *
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @since 2024-02-25
 */
public interface UserFriendService {
    /**
     * 游标翻页获取好友列表
     *
     * @param uid 用户id
     * @param req 请求分页请求
     * @return
     */
    CursorPageBaseResp<FriendResp> getFriendList(Long uid, CursorPageBaseReq req);

    /**
     * 检查是否是好友（批量检查）
     *
     * @param uid 用户id
     * @param req 请求
     * @return
     */
    FriendCheckResp checkUserIsFriend(Long uid, FriendCheckReq req);

    /**
     * 添加好友
     *
     * @param uid 用户id
     * @param req 请求
     */
    void applyAddFriend(Long uid, FriendApplyReq req);

    /**
     * 同意好友申请
     *
     * @param uid
     * @param req
     */
    void applyApprove(Long uid, FriendApproveReq req);

    /**
     * 获取未读取的好友申请数量
     *
     * @param uid
     * @return
     */
    FriendUnreadResp unread(Long uid);

    /**
     * 普通翻页获取好友申请列表
     *
     * @param uid
     * @param req
     * @return
     */
    PageBaseResp<FriendApplyResp> getFriendApplyList(Long uid, PageBaseReq req);

    /**
     * 删除好友 （自是标识状态而不是真正的删除）
     *
     * @param uid
     * @param req
     */
    void deleteFriend(Long uid, FriendDeleteReq req);
}

package com.leikooo.mallchat.common.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.leikooo.mallchat.common.common.domain.vo.request.CursorPageBaseReq;
import com.leikooo.mallchat.common.common.domain.vo.response.ApiResult;
import com.leikooo.mallchat.common.common.domain.vo.response.CursorPageBaseResp;
import com.leikooo.mallchat.common.user.domain.entity.UserFriend;
import com.leikooo.mallchat.common.user.domain.vo.request.friend.FriendCheckReq;
import com.leikooo.mallchat.common.user.domain.vo.response.friend.FriendCheckResp;
import com.leikooo.mallchat.common.user.domain.vo.response.friend.FriendResp;

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
}

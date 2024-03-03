package com.leikooo.mallchat.common.user.controller;

import com.leikooo.mallchat.common.common.domain.vo.request.CursorPageBaseReq;
import com.leikooo.mallchat.common.common.domain.vo.request.PageBaseReq;
import com.leikooo.mallchat.common.common.domain.vo.response.ApiResult;
import com.leikooo.mallchat.common.common.domain.vo.response.CursorPageBaseResp;
import com.leikooo.mallchat.common.common.domain.vo.response.PageBaseResp;
import com.leikooo.mallchat.common.common.utils.RequestHolder;
import com.leikooo.mallchat.common.user.domain.vo.request.friend.FriendApplyReq;
import com.leikooo.mallchat.common.user.domain.vo.request.friend.FriendApproveReq;
import com.leikooo.mallchat.common.user.domain.vo.request.friend.FriendCheckReq;
import com.leikooo.mallchat.common.user.domain.vo.request.friend.FriendDeleteReq;
import com.leikooo.mallchat.common.user.domain.vo.response.friend.FriendApplyResp;
import com.leikooo.mallchat.common.user.domain.vo.response.friend.FriendCheckResp;
import com.leikooo.mallchat.common.user.domain.vo.response.friend.FriendResp;
import com.leikooo.mallchat.common.user.domain.vo.response.friend.FriendUnreadResp;
import com.leikooo.mallchat.common.user.service.UserFriendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/2/26
 * @description
 */
@RestController
@Api("联系人相关接口")
@RequestMapping("/capi/user/friend")
public class FriendController {
    @Resource
    private UserFriendService userFriendService;

    @GetMapping("/check")
    @ApiOperation("批量判断是否是好友")
    public ApiResult<FriendCheckResp> checkUserIsFriend(@RequestBody @Valid FriendCheckReq req) {
        Long uid = RequestHolder.get().getUid();
        return ApiResult.success(userFriendService.checkUserIsFriend(uid, req));
    }

    @GetMapping("/getFriendList")
    @ApiOperation("获取好友列表，通过游标翻页")
    public ApiResult<CursorPageBaseResp<FriendResp>> getFriendList(@RequestBody @Valid CursorPageBaseReq req) {
        Long uid = RequestHolder.get().getUid();
        return ApiResult.success(userFriendService.getFriendList(uid, req));
    }

    @PostMapping("/apply")
    @ApiOperation("申请添加好友")
    public ApiResult<Void> applyAddFriend(@RequestBody @Valid FriendApplyReq req) {
        Long uid = RequestHolder.get().getUid();
        userFriendService.applyAddFriend(uid, req);
        return ApiResult.success();
    }

    @GetMapping("/apply/unread")
    @ApiOperation("获取未读好友申请数量")
    public ApiResult<FriendUnreadResp> getUnreadFriendUnm() {
        Long uid = RequestHolder.get().getUid();
        return ApiResult.success(userFriendService.unread(uid));
    }

    @GetMapping("/apply/page")
    @ApiOperation("好友申请列表")
    public ApiResult<PageBaseResp<FriendApplyResp>> getFriendApplyList(@RequestBody @Valid PageBaseReq req) {
        Long uid = RequestHolder.get().getUid();
        return ApiResult.success(userFriendService.getFriendApplyList(uid, req));
    }

    @DeleteMapping()
    @ApiOperation("删除好友")
    public ApiResult<?> deleteFriend(@RequestBody @Valid FriendDeleteReq req) {
        Long uid = RequestHolder.get().getUid();
        userFriendService.deleteFriend(uid, req);
        return ApiResult.success();
    }

    @PutMapping("/apply")
    @ApiOperation("审批同意")
    public ApiResult<Void> applyApprove(@Valid @RequestBody FriendApproveReq request) {
        userFriendService.applyApprove(RequestHolder.get().getUid(), request);
        return ApiResult.success();
    }
}

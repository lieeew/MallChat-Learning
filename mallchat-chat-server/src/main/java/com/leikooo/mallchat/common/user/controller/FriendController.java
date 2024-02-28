package com.leikooo.mallchat.common.user.controller;

import com.leikooo.mallchat.common.common.domain.vo.request.CursorPageBaseReq;
import com.leikooo.mallchat.common.common.domain.vo.response.ApiResult;
import com.leikooo.mallchat.common.common.domain.vo.response.CursorPageBaseResp;
import com.leikooo.mallchat.common.common.utils.RequestHolder;
import com.leikooo.mallchat.common.user.domain.vo.request.friend.FriendCheckReq;
import com.leikooo.mallchat.common.user.domain.vo.response.friend.FriendCheckResp;
import com.leikooo.mallchat.common.user.domain.vo.response.friend.FriendResp;
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

}

package com.leikooo.mallchat.common.user.controller;


import com.leikooo.mallchat.common.common.domain.vo.response.ApiResult;
import com.leikooo.mallchat.common.common.utils.RequestHolder;
import com.leikooo.mallchat.common.user.domain.vo.request.user.BlockUserReq;
import com.leikooo.mallchat.common.user.domain.vo.request.user.ModifyNameReq;
import com.leikooo.mallchat.common.user.domain.vo.request.user.WearingBadgeReq;
import com.leikooo.mallchat.common.user.domain.vo.response.user.BadgeResp;
import com.leikooo.mallchat.common.user.domain.vo.response.user.UserInfoResp;
import com.leikooo.mallchat.common.user.service.LoginService;
import com.leikooo.mallchat.common.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @since 2024-02-07
 */
@RestController
@RequestMapping("/capi/user")
@Api("用户相关接口")
public class UserController {
    @Resource
    private UserService userService;

    @Resource
    private LoginService loginService;

    @GetMapping("/getUserInfo")
    @ApiOperation("获取用户信息")
    public ApiResult<UserInfoResp> getUserInfo() {
        return ApiResult.success(userService.getUserInfo(RequestHolder.get().getUid()));
    }

    @PutMapping("/modifyName")
    @ApiOperation("修改用户名")
    public ApiResult<Void> modifyName(@Valid @RequestBody ModifyNameReq modifyNameReq) {
        userService.modifyName(RequestHolder.get().getUid(), modifyNameReq.getName());
        return ApiResult.success();
    }

    @GetMapping("/getBadge")
    @ApiOperation("获取徽章信息")
    public ApiResult<List<BadgeResp>> getBadge() {
        return ApiResult.success(userService.getBadge(RequestHolder.get().getUid()));
    }

    @PutMapping("/useItem")
    @ApiOperation("佩戴徽章")
    public ApiResult<Void> useItem(@Valid @RequestBody WearingBadgeReq req) {
        userService.wearingBadge(RequestHolder.get().getUid(), req.getItemId());
        return ApiResult.success();
    }
    @PutMapping("/black")
    @ApiOperation("拉黑用户")
    public ApiResult<Void> blockUser(@Valid @RequestBody BlockUserReq req) {
        Long uid = RequestHolder.get().getUid();
        userService.blockUser(uid, req.getUid());
        return ApiResult.success();
    }

    @Profile("dev")
    @GetMapping("/public/getToken")
    @ApiOperation("获取 token 测试接口")
    public ApiResult<String> getToken(@RequestParam Long uid) {
        return ApiResult.success(loginService.login(uid));
    }
}


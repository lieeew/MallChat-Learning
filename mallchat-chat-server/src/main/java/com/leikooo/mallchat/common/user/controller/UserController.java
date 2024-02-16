package com.leikooo.mallchat.common.user.controller;


import com.leikooo.mallchat.common.common.domain.dto.RequestInfo;
import com.leikooo.mallchat.common.common.domain.vo.response.ApiResult;
import com.leikooo.mallchat.common.common.util.RequestHolder;
import com.leikooo.mallchat.common.user.adapter.UserAdaptor;
import com.leikooo.mallchat.common.user.dao.UserBackpackDao;
import com.leikooo.mallchat.common.user.domain.entity.User;
import com.leikooo.mallchat.common.user.domain.entity.UserBackpack;
import com.leikooo.mallchat.common.user.domain.enums.ItemEnum;
import com.leikooo.mallchat.common.user.domain.vo.request.user.ModifyNameReq;
import com.leikooo.mallchat.common.user.domain.vo.response.user.UserInfoResp;
import com.leikooo.mallchat.common.user.service.LoginService;
import com.leikooo.mallchat.common.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.leikooo.mallchat.common.common.domain.vo.response.ApiResult.success;

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

    @PostMapping("/modifyName")
    @ApiOperation("修改用户名")
    public ApiResult<Void> modifyName(@Valid @RequestBody ModifyNameReq modifyNameReq) {
        userService.modifyName(RequestHolder.get().getUid(), modifyNameReq.getName());
        return ApiResult.success();
    }

    @GetMapping("/public/getToken")
    @ApiOperation("获取 token 测试接口")
    public ApiResult<String> getToken(@RequestParam Long uid) {
        return ApiResult.success(loginService.login(uid));
    }
}


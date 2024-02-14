package com.leikooo.mallchat.common.user.controller;


import com.leikooo.mallchat.common.user.domain.vo.response.user.UserInfoResp;
import com.leikooo.mallchat.common.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

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

    @GetMapping("/public/getUserInfo")
    @ApiOperation("获取用户信息")
    public UserInfoResp getUserInfo(@RequestParam Long userId) {
        return null;
    }

}


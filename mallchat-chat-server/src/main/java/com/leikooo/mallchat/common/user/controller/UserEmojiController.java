package com.leikooo.mallchat.common.user.controller;

import com.leikooo.mallchat.common.common.domain.vo.response.ApiResult;
import com.leikooo.mallchat.common.common.domain.vo.response.IdRespVO;
import com.leikooo.mallchat.common.common.utils.RequestHolder;
import com.leikooo.mallchat.common.user.domain.vo.request.user.UserEmojiDeleteReq;
import com.leikooo.mallchat.common.user.domain.vo.request.user.UserEmojiReq;
import com.leikooo.mallchat.common.user.domain.vo.response.user.UserEmojiResp;
import com.leikooo.mallchat.common.user.service.UserEmojiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/4/5
 * @description
 */
@RestController
@Api(tags = "表情包")
@RequestMapping("/capi/user/emoji")
public class UserEmojiController {
    @Resource
    private UserEmojiService userEmojiService;

    @GetMapping()
    @ApiOperation("展示用户拥有的表情包")
    public ApiResult<List<UserEmojiResp>> list() {
        return ApiResult.success(userEmojiService.list(RequestHolder.get().getUid()));
    }

    @PutMapping()
    @ApiOperation("新增表情包接口")
    public ApiResult<IdRespVO> addEmoji(@Valid @RequestBody UserEmojiReq req) {
        return ApiResult.success(userEmojiService.addEmoji(RequestHolder.get().getUid(), req));
    }

    @DeleteMapping()
    @ApiOperation("删除表情包")
    public ApiResult<Void> deleteEmoji(@Valid @RequestBody UserEmojiDeleteReq req) {
        userEmojiService.deleteEmoji(RequestHolder.get().getUid(), req.getId());
        return ApiResult.success();
    }
}

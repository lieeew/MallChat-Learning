package com.leikooo.mallchat.common.chat.controller;

import com.leikooo.mallchat.common.chat.domain.vo.request.ChatMessageBaseReq;
import com.leikooo.mallchat.common.chat.domain.vo.request.ChatMessageMarkReq;
import com.leikooo.mallchat.common.chat.domain.vo.request.ChatMessagePageReq;
import com.leikooo.mallchat.common.chat.domain.vo.request.ChatMessageReq;
import com.leikooo.mallchat.common.chat.domain.vo.response.ChatMessageResp;
import com.leikooo.mallchat.common.chat.service.ChatService;
import com.leikooo.mallchat.common.common.domain.vo.response.ApiResult;
import com.leikooo.mallchat.common.common.domain.vo.response.CursorPageBaseResp;
import com.leikooo.mallchat.common.common.utils.RequestHolder;
import com.leikooo.mallchat.common.frequency.annotation.FrequencyControl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/3/11
 * @description
 */
@RestController
@Api(tags = "聊天相关接口")
@RequestMapping("/capi/chat")
public class ChatController {
    @Resource
    private ChatService chatService;

    @PostMapping("/sendMsg")
    public ApiResult<Void> sendMsg(@RequestBody @Valid ChatMessageReq req) {
        chatService.sendMsg(RequestHolder.get().getUid(), req);
        return ApiResult.success();
    }

    @FrequencyControl(time = 120, count = 20, target = FrequencyControl.Target.UID, prefixKey = "ChatController#getMsgPage")
    @GetMapping("public/msg/page")
    @ApiOperation("消息列表")
    public ApiResult<CursorPageBaseResp<ChatMessageResp>> getMsgPage(@Valid ChatMessagePageReq req) {
        return ApiResult.success(chatService.getMsgPage(req, RequestHolder.get().getUid()));
    }

    @PostMapping("/msg/recall")
    @ApiOperation("撤回消息")
    public ApiResult<ChatMessageResp> recallMsg(@Valid @RequestBody ChatMessageBaseReq req) {
        chatService.recallMsg(req, RequestHolder.get().getUid());
        return ApiResult.success();
    }

    @PutMapping("/msg/mark")
    @ApiOperation("消息标记")
    public ApiResult<Void> setMsgMark(@Valid @RequestBody ChatMessageMarkReq request) {
        chatService.setMsgMark(RequestHolder.get().getUid(), request);
        return ApiResult.success();
    }
}

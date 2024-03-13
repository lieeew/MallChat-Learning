package com.leikooo.mallchat.common.chat.controller;

import com.leikooo.mallchat.common.chat.domain.vo.request.ChatMessageReq;
import com.leikooo.mallchat.common.chat.service.ChatService;
import com.leikooo.mallchat.common.common.domain.vo.response.ApiResult;
import com.leikooo.mallchat.common.common.utils.RequestHolder;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

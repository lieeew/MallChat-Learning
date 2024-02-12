package com.leikooo.mallchat.common.user.service;

import com.leikooo.mallchat.common.user.domain.enums.WSBaseResp;
import com.leikooo.mallchat.common.user.domain.vo.request.ws.WSAuthorize;
import io.netty.channel.Channel;

public interface WebSocketService {

    /**
     * 处理用户登录请求，需要返回一张带 code 的二维码
     *
     * @param channel
     */
    void handleLoginReq(Channel channel);

    /**
     * 处理所有 ws 连接的事件
     *
     * @param channel
     */
    void connect(Channel channel);

    /**
     * 处理ws断开连接的事件
     *
     * @param channel
     */
    void removed(Channel channel);

    /**
     * 扫码用户登录成功通知,清除本地Cache中的loginCode和channel的关系
     */
    Boolean scanLoginSuccess(Integer loginCode, Long uid);

    void waitAuthorized(Integer code);
}

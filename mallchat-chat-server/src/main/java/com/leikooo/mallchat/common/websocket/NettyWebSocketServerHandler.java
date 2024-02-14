package com.leikooo.mallchat.common.websocket;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.leikooo.mallchat.common.user.domain.enums.WSReqTypeEnum;
import com.leikooo.mallchat.common.user.domain.vo.request.ws.WSAuthorize;
import com.leikooo.mallchat.common.user.domain.vo.request.ws.WSBaseReq;
import com.leikooo.mallchat.common.user.service.WebSocketService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;


/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 */
@Slf4j
@Sharable
public class NettyWebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private WebSocketService webSocketService;

    /**
     * 添加处理器
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        this.webSocketService = SpringUtil.getBean(WebSocketService.class);
        webSocketService.connect(ctx.channel());
    }

    /**
     * 客户端离线
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        userOffLine(ctx);
    }

    /**
     * 取消绑定
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 可能出现业务判断离线后再次触发 channelInactive
        log.warn("触发 channelInactive 掉线![{}]", ctx.channel().id());
        userOffLine(ctx);
    }

    /**
     * 用户离线
     *
     * @param ctx
     */
    private void userOffLine(ChannelHandlerContext ctx) {
        this.webSocketService.removed(ctx.channel());
        ctx.channel().close();
    }


    /**
     * 心跳检查
     *
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                userOffLine(ctx);
            }
        } else if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            Channel channel = ctx.channel();
            String token = NettyUtil.getAttr(channel, NettyUtil.TOKEN);
            if (StringUtils.isEmpty(token)) {
                webSocketService.authorized(channel, token);
            }
        }
        // the method calls super.userEventTriggered(ctx, evt), which passes the event to the next handler in the pipeline
        super.userEventTriggered(ctx, evt);
    }

    /**
     * 处理异常
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.warn("异常发生, 异常消息 = {}", cause.toString());
        ctx.channel().close();
    }

    /**
     * 读取客户端发送的请求报文
     *
     * @param ctx the {@link ChannelHandlerContext} which this {@link SimpleChannelInboundHandler}
     *            belongs to
     * @param msg the message to handle
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        WSBaseReq wsBaseReq = JSONUtil.toBean(msg.text(), WSBaseReq.class);
        WSReqTypeEnum wsReqTypeEnum = WSReqTypeEnum.of(wsBaseReq.getType());
        switch (wsReqTypeEnum) {
            case LOGIN:
                webSocketService.handleLoginReq(ctx.channel());
                break;
            case HEARTBEAT:
                break;
            default:
                log.info("未知类型");
        }
    }
}

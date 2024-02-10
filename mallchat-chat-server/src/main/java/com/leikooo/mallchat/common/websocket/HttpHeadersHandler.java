package com.leikooo.mallchat.common.websocket;

import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.http.HttpUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import org.apache.commons.lang3.StringUtils;

import java.net.InetSocketAddress;
import java.util.Optional;

/**
 * @author leikooo
 */
public class HttpHeadersHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            UrlBuilder urlBuilder = UrlBuilder.ofHttp(request.uri());
            // 拿到 token
            String token = Optional.ofNullable(urlBuilder.getQuery()).map(query -> query.get("token")).map(CharSequence::toString).orElse("");
            NettyUtil.setAttr(ctx.channel(), NettyUtil.TOKEN, token);
            // 拿到请求路径
            request.setUri(urlBuilder.getPath().toString());
            String ip = request.headers().get("X-Real-IP");
            if (StringUtils.isEmpty(ip)) {
                InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
                ip = address.getAddress().getHostAddress();
            }
            NettyUtil.setAttr(ctx.channel(), NettyUtil.IP, ip);
            // 只需要走一次
            ctx.pipeline().remove(this);
        }
        ctx.fireChannelRead(msg);
    }
}
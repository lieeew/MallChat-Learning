package com.leikooo.mallchat.common.websocket;

import cn.hutool.Hutool;
import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.http.HttpRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 */
public class HttpHeadersHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest fullHttpRequest = (FullHttpRequest) msg;
            UrlBuilder urlBuilder = UrlBuilder.ofHttp(fullHttpRequest.uri());
            Optional<String> token = Optional.of(urlBuilder).map(UrlBuilder::getQuery).map(k -> k.get("token")).map(CharSequence::toString);
            token.ifPresent(s -> NettyUtil.setAttr(ctx.channel(), NettyUtil.TOKEN, s));
            fullHttpRequest.setUri(urlBuilder.getPath().toString());
            String ip = fullHttpRequest.headers().get("X-Real-IP");
            if (StringUtils.isNotBlank(ip)) {
                NettyUtil.setAttr(ctx.channel(), NettyUtil.IP, ip);
            }
            ctx.pipeline().remove(this);
        }
        ctx.fireChannelRead(msg);
    }
}
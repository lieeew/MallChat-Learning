package com.leikooo.mallchat.common.websocket;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.util.AttributeKey;

/**
 * Description: netty工具类
 * Date: 2023-04-18
 *
 * @author leikooo
 */

public class NettyUtil {
    public static AttributeKey<String> IP = AttributeKey.valueOf("ip");

    public static AttributeKey<Long> UID = AttributeKey.valueOf("uid");

    public static AttributeKey<WebSocketServerHandshaker> HANDSHAKER_ATTR_KEY = AttributeKey.valueOf(WebSocketServerHandshaker.class, "HANDSHAKER");

    public static AttributeKey<String> TOKEN = AttributeKey.valueOf("token");

    /**
     * 这个函数是一个泛型方法，用于向指定的 Channel 设置指定的 AttributeKey 对应的属性值。
     * 该函数通过调用 Channel 的 attr 方法获取指定 AttributeKey 对应的 Attribute，然后使用 set 方法设置属性值。
     *
     * @param channel
     * @param attributeKey
     * @param data
     * @param <T>
     */
    public static <T> void setAttr(Channel channel, AttributeKey<T> attributeKey, T data) {
        channel.attr(attributeKey).set(data);
    }

    public static <T> T getAttr(Channel channel, AttributeKey<T> ip) {
        return channel.attr(ip).get();
    }
}

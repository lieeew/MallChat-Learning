package com.leikooo.mallchat.common.user.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.leikooo.mallchat.common.common.config.ThreadPoolConfig;
import com.leikooo.mallchat.common.common.event.UserOnlineEvent;
import com.leikooo.mallchat.common.user.adapter.WebSocketAdapter;
import com.leikooo.mallchat.common.user.dao.UserDao;
import com.leikooo.mallchat.common.user.domain.dto.WSChannelExtraDTO;
import com.leikooo.mallchat.common.user.domain.entity.User;
import com.leikooo.mallchat.common.user.domain.enums.WSBaseResp;
import com.leikooo.mallchat.common.user.service.LoginService;
import com.leikooo.mallchat.common.user.service.WebSocketService;
import com.leikooo.mallchat.common.websocket.NettyUtil;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date : 2023-03-19 16:21
 * @description websocket 处理类
 */
@Slf4j
@Component
public class WebSocketServiceImpl implements WebSocketService {
    /**
     * 绑定 Channel 和 WSChannelExtraDTO 的关系，至于为什么不直接使用 UUID 呢，因为这样方便扩展
     * 管理所有的用户连接，包括登录、未登录
     */
    private static final Map<Channel, WSChannelExtraDTO> ONLINE_WS_MAP = new ConcurrentHashMap<>();

    public static final Duration EXPIRE_MINUTES = Duration.ofHours(1);

    public static final long MAX_SIZE = 1000;

    /**
     * 等待登录的用户，存储的是生成二维码的 code 和对应的 Channel
     */
    public static final Cache<Integer, Channel> WAITING_LOGIN_MAP = Caffeine.newBuilder()
            .expireAfterWrite(EXPIRE_MINUTES)
            .maximumSize(MAX_SIZE).build();

    @Lazy
    @Resource
    private WxMpService wxMpService;

    @Resource
    private LoginService loginService;

    @Resource
    private UserDao userDao;

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    /**
     * Qualifier 是 Spring 提供的注解，用于标识要注入的 bean 的名称或限定符
     * 解决 bean 重名问题
     */
    @Resource
    @Qualifier(ThreadPoolConfig.WS_EXECUTOR)
    private ThreadPoolTaskExecutor websocketExecutor;

    @Override
    public void handleLoginReq(Channel channel) {
        // 生成一个随机的 code
        int code = generateCode(channel);
        // 调用 WX 接口生成二维码
        try {
            WxMpQrCodeTicket wxMpQrCodeTicket = wxMpService.getQrcodeService().qrCodeCreateTmpTicket(code, (int) EXPIRE_MINUTES.getSeconds());
            // 二维码发送给前端
            sendMsg(channel, WebSocketAdapter.buildLoginUrlResp(wxMpQrCodeTicket));
        } catch (WxErrorException e) {
            log.error("生成二维码/发送二维码失败", e);
        }
    }

    private void sendMsg(Channel channel, WSBaseResp<?> resp) {
        channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(resp)));
    }


    /**
     * 生成一个随机的 code
     *
     * @param channel
     * @return
     */
    private int generateCode(Channel channel) {
        int code;
        do {
            code = RandomUtil.randomInt(Integer.MAX_VALUE);
        } while (Objects.nonNull(WAITING_LOGIN_MAP.asMap().putIfAbsent(code, channel)));
        return code;
    }


    @Override
    public void connect(Channel channel) {
        WSChannelExtraDTO wsChannelExtraDTO = WSChannelExtraDTO.builder().build();
        ONLINE_WS_MAP.put(channel, wsChannelExtraDTO);
    }

    @Override
    public void removed(Channel channel) {
        // 保证 ONLINE_WS_MAP 不会 OOM
        ONLINE_WS_MAP.remove(channel);
    }

    @Override
    public void scanLoginSuccess(Integer loginCode, Long uid) {
        Channel channel = WAITING_LOGIN_MAP.getIfPresent(loginCode);
        if (Objects.isNull(channel)) {
            // 没有对应的 channel
            return;
        }
        // 移除 code
        WAITING_LOGIN_MAP.invalidate(loginCode);
        // 获取 token
        String token = loginService.login(uid);
        // 获取登录的 user
        User user = userDao.getById(uid);
        loginSuccess(channel, WebSocketAdapter.buildLoginSuccessResp(user, token), user);
    }

    @Override
    public void waitAuthorized(Integer code) {
        Channel channel = WAITING_LOGIN_MAP.getIfPresent(code);
        if (Objects.isNull(channel)) {
            return;
        }
        sendMsg(channel, WebSocketAdapter.buildWaitAuthorizedResp());
    }

    @Override
    public void authorized(Channel channel, String token) {
        Long uid = loginService.getValidUid(token);
        if (uid == null) {
            // token 已经失效
            sendMsg(channel, WebSocketAdapter.buildInvalidToken());
            return;
        }
        User user = userDao.getById(uid);
        loginSuccess(channel, WebSocketAdapter.buildAuthorizedResp(user, token), user);
    }

    @Override
    public void sendToAllOnline(WSBaseResp<?> wsBaseResp) {
        ONLINE_WS_MAP.forEach((channel, wsChannelExtraDTO) -> websocketExecutor.execute(() -> sendMsg(channel, wsBaseResp)));
    }

    @Override
    public void sendToUid(WSBaseResp<?> wsResp, Long uid) {
        ONLINE_WS_MAP.forEach((channel, wsChannelExtraDTO) -> {
            if (uid.equals(wsChannelExtraDTO.getUid())) {
                websocketExecutor.execute(() -> sendMsg(channel, wsResp));
            }
        });
    }

    /**
     * 1、发送登录成功的消息
     * 2、更新 ONLINE_WS_MAP 里面与 channel 绑定的对象的属性
     *
     * @param channel
     * @param user
     */
    private void loginSuccess(Channel channel, WSBaseResp<?> wsBaseResp, User user) {
        WSChannelExtraDTO wsChannelExtraDTO = ONLINE_WS_MAP.get(channel);
        wsChannelExtraDTO.setUid(user.getId());
        // todo ip 相关
        user.setUpdateTime(new Date());
        user.refreshIp(NettyUtil.getAttr(channel, NettyUtil.IP));
        applicationEventPublisher.publishEvent(new UserOnlineEvent(this, user));
        sendMsg(channel, wsBaseResp);
    }
}

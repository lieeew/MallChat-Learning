package com.leikooo.mallchat.common.user.service.impl;

import com.leikooo.mallchat.common.user.adapter.TextBuilder;
import com.leikooo.mallchat.common.user.adapter.UserAdaptor;
import com.leikooo.mallchat.common.user.adapter.WebSocketAdapter;
import com.leikooo.mallchat.common.user.dao.UserDao;
import com.leikooo.mallchat.common.user.domain.entity.User;
import com.leikooo.mallchat.common.user.service.UserService;
import com.leikooo.mallchat.common.user.service.WebSocketService;
import com.leikooo.mallchat.common.user.service.WxMsgService;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/2/10
 * @description
 */
@Slf4j
@Service
public class WxMsgServiceImpl implements WxMsgService {
    @Resource
    private UserDao userDao;

    @Resource
    private UserService userService;

    /**
     * 用户的 openId 和前端登录场景 code 的映射关系
     */
    private static final String URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";

    @Value("${wx.mp.callback}")
    private String callback;

    /**
     * 用户的 openId 和前端登录场景 code 的映射关系, 等待授权
     */
    private static final Map<String, Integer> WAIT_AUTHORIED_MAP = new ConcurrentHashMap<>();

    @Resource
    private WebSocketService webSocketService;

    @Override
    public WxMpXmlOutMessage scan(WxMpXmlMessage wxMpXmlMessage, WxMpService wxMpService) {
        String openId = wxMpXmlMessage.getFromUser();
        User user = userDao.getUserByOpenId(openId);
        Integer code = Integer.parseInt(getEventKey(wxMpXmlMessage));
        WAIT_AUTHORIED_MAP.put(openId, code);
        boolean isHaveUser = Objects.nonNull(user);
        // 用来判断用户是否授权
        boolean authorized = isHaveUser && StringUtils.isNotBlank(user.getAvatar());
        if (authorized) {
            // 已经授权 同时 登录
            webSocketService.scanLoginSuccess(code, user.getId());
            return null;
        }
        // 向前端发送 等待授权的 message
        webSocketService.waitAuthorized(code);
        if (!isHaveUser) {
            userService.saveUser(UserAdaptor.buildNewUser(openId));
        }
        String skipUrl = String.format(URL, wxMpService.getWxMpConfigStorage().getAppId(), URLEncoder.encode(callback + "/wx/portal/public/callBack"));
        WxMpXmlOutMessage.TEXT().build();
        return new TextBuilder().build("请点击链接授权：<a href=\"" + skipUrl + "\">登录</a>", wxMpXmlMessage, wxMpService);
    }

    /**
     * 用户授权 + 更新用户信息
     *
     * @param userInfo
     */
    @Override
    public void authorized(WxOAuth2UserInfo userInfo) {
        User user = userDao.getUserByOpenId(userInfo.getOpenid());
        assert user != null : String.format("用户 openId = %s 不存在!", userInfo.getOpenid());
        if (StringUtils.isEmpty(user.getAvatar())) {
            fillUserInfo(userInfo, user.getId());
        }
        Integer code = WAIT_AUTHORIED_MAP.remove(userInfo.getOpenid());
        webSocketService.scanLoginSuccess(code, user.getId());
    }

    private void fillUserInfo(WxOAuth2UserInfo userInfo, Long uid) {
        User user = UserAdaptor.buildAuthorizedUser(uid, userInfo);
        try {
            userDao.updateById(user);
        } catch (Exception e) {
            log.error("更新用户信息失败", e);
        }
    }

    private String getEventKey(WxMpXmlMessage wxMpXmlMessage) {
        // 扫码关注的渠道事件有前缀，需要去除
        return wxMpXmlMessage.getEventKey().replace("qrscene_", "");
    }
}

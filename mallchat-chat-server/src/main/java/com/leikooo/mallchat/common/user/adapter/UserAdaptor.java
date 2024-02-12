package com.leikooo.mallchat.common.user.adapter;

import com.leikooo.mallchat.common.user.domain.entity.User;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;

import java.util.Date;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/2/10
 * @description
 */
public class UserAdaptor {
    public static User buildNewUser(String openId) {
        return User.builder().openId(openId).build();
    }

    /**
     * 构建授权后的用户信息
     * @param id 用户 id 也叫 uniId
     * @param userInfo 微信用户信息
     * @return
     */
    public static User buildAuthorizedUser(Long id, WxOAuth2UserInfo userInfo) {
        return User.builder()
                .id(id)
                .name(userInfo.getNickname())
                .avatar(userInfo.getHeadImgUrl())
                .sex(userInfo.getSex())
                .updateTime(new Date())
                .build();
    }
}

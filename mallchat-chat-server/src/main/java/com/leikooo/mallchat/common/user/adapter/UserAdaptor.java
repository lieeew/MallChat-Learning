package com.leikooo.mallchat.common.user.adapter;

import com.leikooo.mallchat.common.common.domain.enums.YesOrNoEnum;
import com.leikooo.mallchat.common.user.domain.entity.ItemConfig;
import com.leikooo.mallchat.common.user.domain.entity.User;
import com.leikooo.mallchat.common.user.domain.entity.UserBackpack;
import com.leikooo.mallchat.common.user.domain.vo.response.user.BadgeResp;
import com.leikooo.mallchat.common.user.domain.vo.response.user.UserInfoResp;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;

import java.util.*;
import java.util.stream.Collectors;

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
     *
     * @param id       用户 id 也叫 uniId
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

    public static UserInfoResp buildUserInfoResp(User user, Integer countByValidItemId) {
        return UserInfoResp.builder()
                .modifyNameChance(countByValidItemId)
                .sex(user.getSex())
                .name(user.getName())
                .id(user.getId())
                .avatar(user.getAvatar())
                .build();
    }

    public static List<BadgeResp> buildBadgeResp(List<ItemConfig> itemConfigList, List<UserBackpack> userBadgeList, User user) {
        Set<Long> obtainList = userBadgeList.stream().map(UserBackpack::getItemId).collect(Collectors.toSet());
        return itemConfigList.stream().map(item ->
                        BadgeResp.builder()
                                .describe(item.getDescribe())
                                .id(item.getId())
                                .img(item.getImg())
                                .obtain(obtainList.contains(item.getId()) ? YesOrNoEnum.YES.getStatus() : YesOrNoEnum.NO.getStatus())
                                .wearing(Objects.equals(user.getItemId(), item.getId()) ? YesOrNoEnum.YES.getStatus() : YesOrNoEnum.NO.getStatus())
                                .build()
                )
                .sorted(Comparator.comparing(BadgeResp::getWearing, Comparator.reverseOrder()).thenComparing(BadgeResp::getObtain, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    public static User buildBlackUser(User blockUser) {
        return User.builder()
                .id(blockUser.getId())
                .status(YesOrNoEnum.YES.getStatus())
                .build();
    }
}

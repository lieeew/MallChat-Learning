package com.leikooo.mallchat.common.user.service;

import com.leikooo.mallchat.common.user.domain.entity.User;
import com.leikooo.mallchat.common.user.domain.vo.response.user.BadgeResp;
import com.leikooo.mallchat.common.user.domain.vo.response.user.UserInfoResp;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @since 2024-02-07
 */
public interface UserService {
    Long saveUser(User user);

    UserInfoResp getUserInfo(Long uid);

    void modifyName(Long uid, String name);

    List<BadgeResp> getBadge(Long uid);

    void wearingBadge(Long uid, Long itemId);
}

package com.leikooo.mallchat.common.user.service;

import com.leikooo.mallchat.common.user.domain.entity.User;
import com.leikooo.mallchat.common.user.domain.vo.response.user.UserInfoResp;

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
}

package com.leikooo.mallchat.common.user.service;

import com.leikooo.mallchat.common.user.domain.entity.User;

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
}

package com.leikooo.mallchat.common.user.adapter;

import com.leikooo.mallchat.common.user.domain.entity.User;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/2/10
 * @description
 */
public class UserAdaptor {
    public static User buildNewUser(String openId) {
        return User.builder().openId(openId).build();
    }
}

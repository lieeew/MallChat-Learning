package com.leikooo.mallchat.common.user.dao;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leikooo.mallchat.common.user.domain.entity.User;
import com.leikooo.mallchat.common.user.mapper.UserMapper;
import com.leikooo.mallchat.common.user.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.OptionalLong;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @since 2024-02-07
 */
@Service
public class UserDao extends ServiceImpl<UserMapper, User> {
    public User getUserByOpenId(String openId) {
        return lambdaQuery().eq(User::getOpenId, openId).one();
    }

    public User getUserByName(String name) {
        return lambdaQuery()
                .eq(User::getName, name)
                .one();
    }

    public boolean modifyName(Long uid, String name) {
        return lambdaUpdate()
                .eq(User::getId, uid)
                .set(User::getName, name)
                .update();
    }

    public void wearingBadge(Long uid, Long itemId) {
        lambdaUpdate()
                .eq(User::getId, uid)
                .set(User::getItemId, itemId)
                .update();
    }

    public Integer getUserStatus(Long uid) {
        return lambdaQuery()
                .eq(User::getId, uid)
                .one()
                .getStatus();
    }

    public Long getLastModifyTime(Long uid) {
        return Optional.ofNullable(lambdaQuery()
                        .select(User::getUpdateTime)
                        .eq(User::getId, uid)
                        .one()).map(User::getUpdateTime)
                .map(Date::getTime)
                .orElse(null);
    }
}

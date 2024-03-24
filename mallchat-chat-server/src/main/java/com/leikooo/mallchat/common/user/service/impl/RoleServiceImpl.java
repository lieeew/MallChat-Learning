package com.leikooo.mallchat.common.user.service.impl;

import com.leikooo.mallchat.common.user.domain.enums.UserRoleEnum;
import com.leikooo.mallchat.common.user.service.RoleService;
import com.leikooo.mallchat.common.user.service.cache.UserCache;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/2/23
 * @description
 */
@Service
public class RoleServiceImpl implements RoleService {
    @Resource
    private UserCache userCache;

    @Override
    public boolean hasPower(Long uid, UserRoleEnum userRoleEnum) {
        return isAdmin(uid) || userCache.getRoles(uid).contains(userRoleEnum.getCode());
    }

    public boolean isAdmin(Long uid) {
        return userCache.getRoles(uid).contains(UserRoleEnum.ADMIN.getCode());
    }
}

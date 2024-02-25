package com.leikooo.mallchat.common.user.service.impl;

import com.leikooo.mallchat.common.user.domain.enums.RoleEnum;
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
    public boolean hasPower(Long uid, RoleEnum roleEnum) {
        return isAdmin(uid) || userCache.getRoles(uid).contains(roleEnum.getId());
    }

    public boolean isAdmin(Long uid) {
        return userCache.getRoles(uid).contains(RoleEnum.ADMIN.getId());
    }
}

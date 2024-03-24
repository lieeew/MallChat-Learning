package com.leikooo.mallchat.common.user.service;

import com.leikooo.mallchat.common.user.domain.enums.UserRoleEnum;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @since 2024-02-23
 */
public interface RoleService {
    /**
     * 判断是否有权限
     *
     * @param uid uid
     * @param userRoleEnum
     * @return
     */
    boolean hasPower(Long uid, UserRoleEnum userRoleEnum);
}

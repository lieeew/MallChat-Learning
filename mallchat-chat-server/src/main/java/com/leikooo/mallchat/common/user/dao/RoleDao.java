package com.leikooo.mallchat.common.user.dao;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leikooo.mallchat.common.user.domain.entity.Role;
import com.leikooo.mallchat.common.user.mapper.RoleMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @since 2024-02-23
 */
@Service
public class RoleDao extends ServiceImpl<RoleMapper, Role> {

}

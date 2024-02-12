package com.leikooo.mallchat.common.user.service.impl;

import com.leikooo.mallchat.common.user.service.LoginService;
import com.leikooo.mallchat.common.util.JwtUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/2/11
 * @description
 */
@Service
public class LoginServiceImpl implements LoginService {
    @Resource
    private JwtUtils jwtUtils;

    @Override
    public String login(Long uid) {
        return jwtUtils.createToken(uid);
    }

    @Override
    public boolean verify(String token) {
        return false;
    }

    @Override
    public void renewalTokenIfNecessary(String token) {

    }

    @Override
    public Long getValidUid(String token) {
        return null;
    }
}

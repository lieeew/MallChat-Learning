package com.leikooo.mallchat.common.user.service.impl;

import com.leikooo.mallchat.common.common.constant.RedisKey;
import com.leikooo.mallchat.common.user.service.LoginService;
import com.leikooo.mallchat.common.common.util.JwtUtils;
import com.leikooo.mallchat.common.common.util.RedisUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/2/11
 * @description
 */
@Service
public class LoginServiceImpl implements LoginService {
    public static final int TOKEN_EXPIRE_TIME = 3;

    @Resource
    private JwtUtils jwtUtils;

    @Override
    public String login(Long uid) {
        String token = jwtUtils.createToken(uid);
        RedisUtils.set(getUserTokenKey(token), token, TOKEN_EXPIRE_TIME, TimeUnit.DAYS);
        return token;
    }

    @Override
    public boolean verify(String token) {
        Long uid = jwtUtils.getUidOrNull(token);
        if (Objects.isNull(uid)) {
            return false;
        }
        String key = getUserTokenKey(token);
        String realToken = RedisUtils.getStr(key);
        // 有可能token失效了，需要校验是不是和最新token一致
        return Objects.equals(token, realToken);
    }

    @Override
    @Async
    public void renewalTokenIfNecessary(String token) {
        Long uid = getValidUid(token);
        Long expire = RedisUtils.getExpire(getUserTokenKey(token));
        if (expire == -2) {
            // expire 是 -2 说明不存在 key
            return;
        }
        if (expire <= 1) {
            String newToken = jwtUtils.createToken(uid);
            RedisUtils.set(getUserTokenKey(token), newToken, TOKEN_EXPIRE_TIME, TimeUnit.DAYS);
        }
    }

    @Override
    public Long getValidUid(String token) {
        boolean verify = this.verify(token);
        if (!verify) {
            return null;
        }
        return jwtUtils.getUidOrNull(token);
    }

    /**
     * 获取用户 token 存储到 redis 的 key
     *
     * @param token token
     * @return 类似: user:token:uid_123
     */
    private String getUserTokenKey(String token) {
        return RedisKey.getKey(RedisKey.USER_TOKEN, token);
    }
}

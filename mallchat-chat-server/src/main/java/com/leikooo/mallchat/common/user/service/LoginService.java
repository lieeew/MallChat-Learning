package com.leikooo.mallchat.common.user.service;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/2/11
 * @description
 */
public interface LoginService {
    /**
     * 登录成功，获取token
     *
     * @param uid
     * @return 返回token
     */
    String login(Long uid);

    /**
     * 校验 token 是不是有效
     *
     * @param token
     * @return
     */
    boolean verify(String token);

    /**
     * 刷新 token 有效期
     *
     * @param token
     */
    void renewalTokenIfNecessary(String token);

    /**
     * 如果token有效，返回uid
     *
     * @param token
     * @return
     */
    Long getValidUid(String token);
}

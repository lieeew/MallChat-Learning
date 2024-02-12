package com.leikooo.mallchat.common.user.domain.vo.response.ws;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @description 登录成功返回的包装类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WSLoginSuccess {
    private Long uid;

    private String avatar;

    private String token;

    private String name;

    /**
     * todo 用户权限 0普通用户 1超管
     */
    private Integer power;
}

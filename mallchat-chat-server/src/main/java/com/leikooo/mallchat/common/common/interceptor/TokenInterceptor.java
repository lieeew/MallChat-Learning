package com.leikooo.mallchat.common.common.interceptor;

import com.leikooo.mallchat.common.common.exception.HttpErrorEnum;
import com.leikooo.mallchat.common.user.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.Optional;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/2/15
 * @description 拦截器
 */
@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {
    public static final String HEADER_AUTHORIZATION = "Authorization";

    public static final String AUTHORIZATION_SCHEMA = "Bearer ";

    public static final String UID = "uid";

    @Resource
    private LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("TokenInterceptor preHandle called");
        String token = getToken(request);
        Long uid = loginService.getValidUid(token);
        if (Objects.nonNull(uid)) {
            request.setAttribute(UID, uid);
            return true;
        }
        boolean isNeedLogin = isNeedLogin(request);
        if (isNeedLogin) {
            return true;
        }
        // 没有登录 & 是需要登录权限的接口
        HttpErrorEnum.ACCESS_DEFINE.sendHttpError(response);
        return false;
    }

    /**
     * 校验对应的接口是否需要登录 比如 /capi/user/public/getUserInfo
     *
     * @param request
     * @return 返回 true 表示需要不登录， false 需要登录
     */
    private boolean isNeedLogin(HttpServletRequest request) {
        String contextPath = request.getRequestURI();
        String[] strings = contextPath.split("/");
        return strings.length > 2 && "public".equals(strings[3]);
    }

    private String getToken(HttpServletRequest request) {
        String header = request.getHeader(HEADER_AUTHORIZATION);
        return Optional.ofNullable(header)
                .filter(f -> f.startsWith(AUTHORIZATION_SCHEMA))
                .map(m -> m.substring(AUTHORIZATION_SCHEMA.length()))
                .orElse(null);
    }
}

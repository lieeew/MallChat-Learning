package com.leikooo.mallchat.common.common.interceptor;

import cn.hutool.extra.servlet.ServletUtil;
import com.leikooo.mallchat.common.common.domain.dto.RequestInfo;
import com.leikooo.mallchat.common.common.util.RequestHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.Optional;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/2/16
 * @description
 */
@Slf4j
@Component
public class CollectorInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("CollectorInterceptor preHandle called");
        Long uid = Optional.ofNullable(request.getAttribute(TokenInterceptor.UID)).map(Objects::toString).map(Long::parseLong).orElse(null);
        // 拿到 ip
        String ip = ServletUtil.getClientIP(request);
        RequestInfo requestInfo = RequestInfo.builder().ip(ip).uid(uid).build();
        RequestHolder.set(requestInfo);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        RequestHolder.remove();
    }
}

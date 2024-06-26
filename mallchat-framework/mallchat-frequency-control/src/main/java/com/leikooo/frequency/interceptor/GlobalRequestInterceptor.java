package com.leikooo.frequency.interceptor;

import cn.hutool.extra.servlet.ServletUtil;
import com.leikooo.frequency.domain.dto.RequestInfo;
import com.leikooo.frequency.util.RequestHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.Optional;

/**
 * @author leikooo
 */
@Slf4j
@Component
public class GlobalRequestInterceptor implements HandlerInterceptor {

    public static final String UID =  "uid";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("malChat-frequency-control GlobalRequestInterceptor preHandle called");
        Long uid = Optional.ofNullable(request.getAttribute(UID)).map(Objects::toString).map(Long::parseLong).orElse(null);
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

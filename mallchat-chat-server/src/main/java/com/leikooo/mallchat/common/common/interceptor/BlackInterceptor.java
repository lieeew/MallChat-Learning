package com.leikooo.mallchat.common.common.interceptor;

import cn.hutool.core.collection.CollectionUtil;
import com.leikooo.mallchat.common.common.domain.dto.RequestInfo;
import com.leikooo.mallchat.common.common.exception.HttpErrorEnum;
import com.leikooo.mallchat.common.common.utils.RequestHolder;
import com.leikooo.mallchat.common.user.dao.BlackDao;
import com.leikooo.mallchat.common.user.dao.UserDao;
import com.leikooo.mallchat.common.user.domain.entity.Black;
import com.leikooo.mallchat.common.user.domain.entity.IpInfo;
import com.leikooo.mallchat.common.user.domain.entity.User;
import com.leikooo.mallchat.common.user.domain.enums.BlackTypeEnum;
import com.leikooo.mallchat.common.user.service.cache.UserCache;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/2/24
 * @description
 */
@Component
public class BlackInterceptor implements HandlerInterceptor {
    @Resource
    private UserCache userCache;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<Integer, Set<String>> blackMap = userCache.getBlackMap();
        RequestInfo requestInfo = RequestHolder.get();
        if (inBlackList(requestInfo.getUid(), blackMap.get(BlackTypeEnum.UID.getType()))) {
            HttpErrorEnum.USER_LOGIN_ACCESS_DEFINE.sendHttpError(response);
            return false;
        }
        if (inBlackList(requestInfo.getIp(), blackMap.get(BlackTypeEnum.IP.getType()))) {
            HttpErrorEnum.USER_LOGIN_ACCESS_DEFINE.sendHttpError(response);
            return false;
        }
        return true;
    }

    private boolean inBlackList(Object target, Set<String> blackSet) {
        if (Objects.isNull(target) || CollectionUtil.isEmpty(blackSet)) {
            return false;
        }
        return blackSet.contains(target.toString());
    }

}

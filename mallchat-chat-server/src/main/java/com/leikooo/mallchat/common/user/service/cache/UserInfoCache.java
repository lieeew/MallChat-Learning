package com.leikooo.mallchat.common.user.service.cache;

import com.leikooo.mallchat.common.common.constant.RedisKey;
import com.leikooo.mallchat.common.common.service.cache.AbstractRedisStringBatchCache;
import com.leikooo.mallchat.common.user.dao.UserDao;
import com.leikooo.mallchat.common.user.domain.entity.User;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/3/5
 * @description
 */
@Component
public class UserInfoCache extends AbstractRedisStringBatchCache<Long, User> {
    @Resource
    private UserDao userDao;

    @Override
    protected String getKey(Long req) {
        return RedisKey.getKey(RedisKey.USER_INFO_STRING, req);
    }

    @Override
    protected long getExpireSeconds() {
        return 5 * 60L;
    }

    @Override
    protected Map<Long, User> load(List<Long> req) {
        List<User> needLoadUserList = userDao.listByIds(req);
        return needLoadUserList.stream().collect(Collectors.toMap(User::getId, Function.identity()));
    }
}

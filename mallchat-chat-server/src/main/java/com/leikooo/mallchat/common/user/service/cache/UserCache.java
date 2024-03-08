package com.leikooo.mallchat.common.user.service.cache;

import com.leikooo.mallchat.common.common.constant.RedisKey;
import com.leikooo.mallchat.common.common.utils.RedisUtils;
import com.leikooo.mallchat.common.user.dao.BlackDao;
import com.leikooo.mallchat.common.user.dao.UserRoleDao;
import com.leikooo.mallchat.common.user.domain.entity.Black;
import com.leikooo.mallchat.common.user.domain.entity.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/2/18
 * @description
 */
@Slf4j
@Component
public class UserCache {
    @Resource
    private UserRoleDao userRoleDao;

    @Resource
    private BlackDao blackDao;

    @Cacheable(value = "userCache", key = "'roles' + #uid")
    public Set<Long> getRoles(Long uid) {
        List<UserRole> userRoles = userRoleDao.listByUid(uid);
        return userRoles.stream()
                .map(UserRole::getRoleId)
                .collect(Collectors.toSet());
    }

    @Cacheable(value = "userCache", key = "'blackMap'")
    public HashMap<Integer, Set<String>> getBlackMap() {
        Map<Integer, List<Black>> map = blackDao.list().stream().collect(Collectors.groupingBy(Black::getType));
        HashMap<Integer, Set<String>> hashMap = new HashMap<>();
        map.forEach((type, list) -> hashMap.put(type, list.stream().map(Black::getTarget).collect(Collectors.toSet())));
        return hashMap;
    }

    @CacheEvict(value = "userCache", key = "'blackMap'")
    public void evictBlackMap() {
        log.info("black map has evict");
    }

    public List<Long> getUserModifyTime(List<Long> uidList) {
        List<String> keys = uidList.stream().map(uid -> RedisKey.getKey(RedisKey.USER_MODIFY_STRING, uid)).collect(Collectors.toList());
        return RedisUtils.mget(keys, Long.class);
    }
}

package com.leikooo.mallchat.common.user.service.cache;

import com.leikooo.mallchat.common.common.constant.RedisKey;
import com.leikooo.mallchat.common.common.utils.RedisUtils;
import com.leikooo.mallchat.common.user.dao.BlackDao;
import com.leikooo.mallchat.common.user.dao.UserDao;
import com.leikooo.mallchat.common.user.dao.UserRoleDao;
import com.leikooo.mallchat.common.user.domain.entity.Black;
import com.leikooo.mallchat.common.user.domain.entity.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
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

    @Resource
    private UserDao userDao;

    @Resource
    private UserSummaryCache userSummaryCache;

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

    public Map<Long, Long> getUserModifyTime(List<Long> uidList) {
        List<String> keys = uidList.stream().map(uid -> RedisKey.getKey(RedisKey.USER_MODIFY_STRING, uid)).collect(Collectors.toList());
        List<Long> lastModifyTime = RedisUtils.mget(keys, Long.class);
        HashMap<Long, Long> userModifyTimeMap = new HashMap<>();
        for (int i = 0; i < uidList.size(); i++) {
            userModifyTimeMap.put(uidList.get(i), lastModifyTime.get(i));
        }
        uidList.forEach(uid -> {
            if (Objects.isNull(userModifyTimeMap.get(uid))) {
                // 获取用户最后修改的时间设置到 redis 之中
                Optional.ofNullable(userDao.getLastModifyTime(uid)).ifPresent(time -> {
                    refreshUserModifyTime(uid, time);
                    userModifyTimeMap.put(uid, time);
                });
            }
        });
        return userModifyTimeMap;
    }

    public void refreshUserModifyTime(Long uid) {
        RedisUtils.set(RedisKey.getKey(RedisKey.USER_MODIFY_STRING, uid), new Date().getTime());
    }

    public void refreshUserModifyTime(Long uid, Long time) {
        RedisUtils.set(RedisKey.getKey(RedisKey.USER_MODIFY_STRING, uid), time);
    }

    public void delUserInfo(Long uid) {
        String key = RedisKey.getKey(RedisKey.USER_INFO_STRING, uid);
        RedisUtils.del(key);
    }

    public void userInfoChange(Long uid) {
        delUserInfo(uid);
        // 删除UserSummaryCache，前端下次懒加载的时候可以获取到最新的数据
        userSummaryCache.delete(uid);
        refreshUserModifyTime(uid);
    }
}

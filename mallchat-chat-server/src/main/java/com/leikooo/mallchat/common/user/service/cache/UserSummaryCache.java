package com.leikooo.mallchat.common.user.service.cache;

import cn.hutool.core.util.ObjectUtil;
import com.leikooo.mallchat.common.common.constant.RedisKey;
import com.leikooo.mallchat.common.common.service.cache.AbstractRedisStringBatchCache;
import com.leikooo.mallchat.common.user.dao.UserBackpackDao;
import com.leikooo.mallchat.common.user.domain.dto.SummeryInfoDTO;
import com.leikooo.mallchat.common.user.domain.entity.*;
import com.leikooo.mallchat.common.user.domain.enums.ItemTypeEnum;
import com.leikooo.mallchat.common.user.domain.vo.request.user.SummeryInfoReq;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/3/5
 * @description
 */
@Component
public class UserSummaryCache extends AbstractRedisStringBatchCache<Long, SummeryInfoDTO> {
    @Resource
    private UserInfoCache userInfoCache;

    @Resource
    private ItemCache itemCache;

    @Resource
    private UserBackpackDao userBackpackDao;

    @Override
    protected String getKey(Long req) {
        return RedisKey.getKey(RedisKey.USER_SUMMARY_STRING, req);
    }

    @Override
    protected long getExpireSeconds() {
        return 10 * 60L;
    }

    @Override
    protected Map<Long, SummeryInfoDTO> load(List<Long> req) {
        Map<Long, User> userInfo = userInfoCache.load(req);
        List<ItemConfig> itemConfigs = itemCache.getByType(ItemTypeEnum.BADGE.getType());
        List<Long> itemIds = itemConfigs.stream().map(ItemConfig::getId).collect(Collectors.toList());
        List<UserBackpack> userBackpacks = userBackpackDao.listByIds(itemIds);
        Map<Long, List<UserBackpack>> userBackMap = userBackpacks.stream().collect(Collectors.groupingBy(UserBackpack::getUid));
        return req.stream()
                .map(uid -> {
                    User user = userInfo.get(uid);
                    if (ObjectUtil.isNull(user)) {
                        return null;
                    }
                    return SummeryInfoDTO.builder()
                            .avatar(user.getAvatar())
                            .locPlace(Optional.of(user).map(User::getIpInfo).map(IpInfo::getUpdateIpDetail).map(IpDetail::getCity).orElse(null))
                            .name(user.getName())
                            .uid(user.getId())
                            .itemIds(userBackMap.get(user.getId()).stream().map(UserBackpack::getItemId).collect(Collectors.toList()))
                            .wearingItemId(user.getItemId())
                            .build();
                }).filter(ObjectUtil::isNotNull)
                .collect(Collectors.toMap(SummeryInfoDTO::getUid, Function.identity()));
    }
}

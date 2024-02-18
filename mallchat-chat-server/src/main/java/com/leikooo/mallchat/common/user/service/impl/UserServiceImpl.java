package com.leikooo.mallchat.common.user.service.impl;

import com.leikooo.mallchat.common.common.util.AssertUtil;
import com.leikooo.mallchat.common.user.adapter.UserAdaptor;
import com.leikooo.mallchat.common.user.dao.ItemConfigDao;
import com.leikooo.mallchat.common.user.dao.UserBackpackDao;
import com.leikooo.mallchat.common.user.dao.UserDao;
import com.leikooo.mallchat.common.user.domain.entity.ItemConfig;
import com.leikooo.mallchat.common.user.domain.entity.User;
import com.leikooo.mallchat.common.user.domain.entity.UserBackpack;
import com.leikooo.mallchat.common.user.domain.enums.ItemEnum;
import com.leikooo.mallchat.common.user.domain.enums.ItemTypeEnum;
import com.leikooo.mallchat.common.user.domain.vo.response.user.BadgeResp;
import com.leikooo.mallchat.common.user.domain.vo.response.user.UserInfoResp;
import com.leikooo.mallchat.common.user.service.UserService;
import com.leikooo.mallchat.common.user.service.cache.ItemCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/2/10
 * @description
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao userDao;

    @Resource
    private UserBackpackDao userBackpackDao;

    @Resource
    private ItemCache itemCache;

    @Resource
    private ItemConfigDao itemConfigDao;

    @Transactional
    @Override
    public Long saveUser(User user) {
        userDao.save(user);
        // todo 推送注册成功事件
        return user.getId();
    }

    @Override
    public UserInfoResp getUserInfo(Long uid) {
        User user = userDao.getById(uid);
        Integer countByValidItemId = userBackpackDao.countByValidItemId(uid, ItemEnum.MODIFY_NAME_CARD.getId());
        return UserAdaptor.buildUserInfoResp(user, countByValidItemId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void modifyName(Long uid, String name) {
        User user = userDao.getUserByName(name);
        AssertUtil.isNotEmpty(user, "用户名已经存在");
        UserBackpack firstValidItem = userBackpackDao.getFirstValidItem(uid, ItemEnum.MODIFY_NAME_CARD.getId());
        AssertUtil.isEmpty(firstValidItem, "没有修改名字的道具, 等活动获取~");
        // 使用改名卡
        boolean result = userBackpackDao.useItem(firstValidItem.getId());
        AssertUtil.isTrue(result, "使用改名卡失败");
        boolean modifyName = userDao.modifyName(uid, name);
        AssertUtil.isTrue(modifyName, "修改用户名失败");
    }

    @Override
    public List<BadgeResp> getBadge(Long uid) {
        // 徽章列表
        List<ItemConfig> itemConfigList = itemCache.getByType(ItemTypeEnum.BADGE.getType());
        // 用户拥有的徽章
        List<UserBackpack> userBadgeList = userBackpackDao.getUserBadge(uid, itemConfigList.stream().map(ItemConfig::getId).collect(Collectors.toSet()));
        // 用户佩戴的徽章
        User user = userDao.getById(uid);
        return UserAdaptor.buildBadgeResp(itemConfigList, userBadgeList, user);
    }

    @Override
    public void wearingBadge(Long uid, Long itemId) {
        UserBackpack userBackpack = userBackpackDao.getFirstValidItem(uid, itemId);
        AssertUtil.isNotEmpty(userBackpack, "您还没有该徽章, 快去活动获取吧~");
        ItemConfig itemConfig = itemConfigDao.getById(userBackpack.getItemId());
        AssertUtil.isNotEmpty(itemConfig, "该徽章不存在");
        // 佩戴徽章
        userDao.wearingBadge(uid, itemId);
    }
}

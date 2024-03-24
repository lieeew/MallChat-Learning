package com.leikooo.mallchat.common.user.service.impl;

import com.leikooo.mallchat.common.common.event.UserBlackEvent;
import com.leikooo.mallchat.common.common.event.UserRegisterEvent;
import com.leikooo.mallchat.common.common.utils.AssertUtil;
import com.leikooo.mallchat.common.user.adapter.UserAdaptor;
import com.leikooo.mallchat.common.user.dao.BlackDao;
import com.leikooo.mallchat.common.user.dao.ItemConfigDao;
import com.leikooo.mallchat.common.user.dao.UserBackpackDao;
import com.leikooo.mallchat.common.user.dao.UserDao;
import com.leikooo.mallchat.common.user.domain.dto.SummeryInfoDTO;
import com.leikooo.mallchat.common.user.domain.entity.*;
import com.leikooo.mallchat.common.user.domain.enums.BlackTypeEnum;
import com.leikooo.mallchat.common.user.domain.enums.ItemEnum;
import com.leikooo.mallchat.common.user.domain.enums.ItemTypeEnum;
import com.leikooo.mallchat.common.user.domain.enums.UserRoleEnum;
import com.leikooo.mallchat.common.user.domain.vo.request.user.SummeryInfoReq;
import com.leikooo.mallchat.common.user.domain.vo.response.user.BadgeResp;
import com.leikooo.mallchat.common.user.domain.vo.response.user.UserInfoResp;
import com.leikooo.mallchat.common.user.service.RoleService;
import com.leikooo.mallchat.common.user.service.UserService;
import com.leikooo.mallchat.common.user.service.cache.ItemCache;
import com.leikooo.mallchat.common.user.service.cache.UserCache;
import com.leikooo.mallchat.common.user.service.cache.UserSummaryCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
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

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @Resource
    private RoleService roleService;

    @Resource
    private BlackDao blackDao;

    @Resource
    private UserCache userCache;

    @Resource
    private UserSummaryCache userSummaryCache;

    @Transactional
    @Override
    public Long saveUser(User user) {
        userDao.save(user);
        // 推送注册成功事件
        applicationEventPublisher.publishEvent(new UserRegisterEvent(this, user));
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
        AssertUtil.isEmpty(user, "用户名已经存在");
        UserBackpack firstValidItem = userBackpackDao.getFirstValidItem(uid, ItemEnum.MODIFY_NAME_CARD.getId());
        AssertUtil.isNotEmpty(firstValidItem, "没有修改名字的道具, 等活动获取~");
        // 使用改名卡
        boolean result = userBackpackDao.useItem(firstValidItem.getId());
        AssertUtil.isTrue(result, "使用改名卡失败");
        boolean modifyName = userDao.modifyName(uid, name);
        AssertUtil.isTrue(modifyName, "修改用户名失败");
        // 刷新缓存
        userCache.userInfoChange(uid);
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
        userCache.userInfoChange(uid);
    }

    /**
     * 封禁用户
     *
     * @param uid      操作者 uid
     * @param blockUid 被封号 uid
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void blockUser(Long uid, Integer blockUid) {
        boolean hasPower = roleService.hasPower(uid, UserRoleEnum.CHAT_MANAGER);
        AssertUtil.isTrue(hasPower, "您没有权限");
        Black insertBlack = Black.builder()
                .type(BlackTypeEnum.UID.getType())
                .target(blockUid.toString())
                .build();
        blackDao.save(insertBlack);
        User blockUser = userDao.getById(blockUid);
        String creatIp = Optional.ofNullable(blockUser).map(User::getIpInfo).map(IpInfo::getCreateIp).orElse("");
        String updateIp = Optional.ofNullable(blockUser).map(User::getIpInfo).map(IpInfo::getUpdateIp).orElse("");
        if (StringUtils.equals(creatIp, updateIp)) {
            blockIpIfNotEmpty(creatIp);
        } else {
            blockIpIfNotEmpty(creatIp);
            blockIpIfNotEmpty(updateIp);
        }
        applicationEventPublisher.publishEvent(new UserBlackEvent(this, blockUser));
    }

    @Override
    public List<SummeryInfoDTO> getSummeryUserInfo(SummeryInfoReq summeryInfoReq) {
        List<SummeryInfoReq.infoReq> reqList = summeryInfoReq.getReqList();
        List<Long> needLoadUidList = needLoad(reqList);
        Map<Long, SummeryInfoDTO> batch = userSummaryCache.getBatch(needLoadUidList);
        return reqList.stream()
                .map(uid -> needLoadUidList.contains(uid.getUid()) ? batch.get(uid.getUid()) : SummeryInfoDTO.skip(uid.getUid()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private List<Long> needLoad(List<SummeryInfoReq.infoReq> reqList) {
        List<Long> uidList = reqList.stream().map(SummeryInfoReq.infoReq::getUid).collect(Collectors.toList());
        Map<Long, Long> userModifyTimeMap = userCache.getUserModifyTime(uidList);
        return reqList.stream()
                .filter(infoReq -> {
                    // 检测刷新时间
                    Long lastModifyTime = Optional.ofNullable(infoReq).map(SummeryInfoReq.infoReq::getLastModifyTime).orElse(null);
                    if (Objects.isNull(lastModifyTime) || Objects.isNull(userModifyTimeMap.get(infoReq.getUid()))) {
                        // 没有刷新时间，直接返回对应的 uid
                        return true;
                    }
                    // 检测是否需要刷新
                    return lastModifyTime < userModifyTimeMap.get(infoReq.getUid());
                }).filter(Objects::nonNull)
                .map(SummeryInfoReq.infoReq::getUid)
                .collect(Collectors.toList());
    }

    private void blockIpIfNotEmpty(String ip) {
        if (StringUtils.isNotEmpty(ip)) {
            Black build = Black.builder()
                    .type(BlackTypeEnum.IP.getType())
                    .target(ip)
                    .build();
            blackDao.save(build);
        }
    }
}

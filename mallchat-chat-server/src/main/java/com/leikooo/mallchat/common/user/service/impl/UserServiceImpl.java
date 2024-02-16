package com.leikooo.mallchat.common.user.service.impl;

import com.leikooo.mallchat.common.common.exception.BusinessException;
import com.leikooo.mallchat.common.common.util.AssertUtil;
import com.leikooo.mallchat.common.user.adapter.UserAdaptor;
import com.leikooo.mallchat.common.user.dao.UserBackpackDao;
import com.leikooo.mallchat.common.user.dao.UserDao;
import com.leikooo.mallchat.common.user.domain.entity.User;
import com.leikooo.mallchat.common.user.domain.entity.UserBackpack;
import com.leikooo.mallchat.common.user.domain.enums.ItemEnum;
import com.leikooo.mallchat.common.user.domain.vo.response.user.UserInfoResp;
import com.leikooo.mallchat.common.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Objects;

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
}

package com.leikooo.mallchat.common.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leikooo.mallchat.common.common.annotation.RedissonLock;
import com.leikooo.mallchat.common.common.domain.vo.request.CursorPageBaseReq;
import com.leikooo.mallchat.common.common.domain.vo.request.PageBaseReq;
import com.leikooo.mallchat.common.common.domain.vo.response.CursorPageBaseResp;
import com.leikooo.mallchat.common.common.domain.vo.response.PageBaseResp;
import com.leikooo.mallchat.common.common.event.UserApplyEvent;
import com.leikooo.mallchat.common.common.utils.AssertUtil;
import com.leikooo.mallchat.common.user.adapter.FriendAdapter;
import com.leikooo.mallchat.common.user.adapter.UserApplyAdapter;
import com.leikooo.mallchat.common.user.adapter.UserFriendAdapter;
import com.leikooo.mallchat.common.user.dao.UserApplyDao;
import com.leikooo.mallchat.common.user.dao.UserDao;
import com.leikooo.mallchat.common.user.dao.UserFriendDao;
import com.leikooo.mallchat.common.user.domain.entity.User;
import com.leikooo.mallchat.common.user.domain.entity.UserApply;
import com.leikooo.mallchat.common.user.domain.entity.UserFriend;
import com.leikooo.mallchat.common.user.domain.enums.ApplyReadStatusEnum;
import com.leikooo.mallchat.common.user.domain.enums.ApplyStatusEnum;
import com.leikooo.mallchat.common.user.domain.enums.UserStatusEnum;
import com.leikooo.mallchat.common.user.domain.vo.request.friend.FriendApplyReq;
import com.leikooo.mallchat.common.user.domain.vo.request.friend.FriendApproveReq;
import com.leikooo.mallchat.common.user.domain.vo.request.friend.FriendCheckReq;
import com.leikooo.mallchat.common.user.domain.vo.request.friend.FriendDeleteReq;
import com.leikooo.mallchat.common.user.domain.vo.response.friend.FriendApplyResp;
import com.leikooo.mallchat.common.user.domain.vo.response.friend.FriendCheckResp;
import com.leikooo.mallchat.common.user.domain.vo.response.friend.FriendResp;
import com.leikooo.mallchat.common.user.domain.vo.response.friend.FriendUnreadResp;
import com.leikooo.mallchat.common.user.service.RoomFriendService;
import com.leikooo.mallchat.common.user.service.UserFriendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/2/26
 * @description
 */
@Slf4j
@Service
public class UserFriendServiceImpl implements UserFriendService {
    @Resource
    private UserFriendDao userFriendDao;

    @Resource
    private UserDao userDao;

    @Resource
    private UserApplyDao userApplyDao;

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @Resource
    private RoomFriendService roomFriendService;

    @Override
    public CursorPageBaseResp<FriendResp> getFriendList(Long uid, CursorPageBaseReq req) {
        CursorPageBaseResp<UserFriend> userFriendList = userFriendDao.getUserFriendList(uid, req);
        List<Long> friendUidList = userFriendList.getList().stream().map(UserFriend::getFriendUid).collect(Collectors.toList());
        List<User> userStatusList = friendUidList.stream().map(friendUid -> User.builder().id(friendUid).status(userDao.getUserStatus(friendUid)).build()).collect(Collectors.toList());
        return CursorPageBaseResp.init(userFriendList, FriendAdapter.buildFriendResp(userFriendList.getList(), userStatusList));
    }

    @Override
    public FriendCheckResp checkUserIsFriend(Long uid, FriendCheckReq req) {
        List<UserFriend> myFriendList = userFriendDao.getMyFriendList(uid, req.getUidList());
        Set<Long> myFriendUidSet = myFriendList.stream().map(UserFriend::getFriendUid).collect(Collectors.toSet());
        List<FriendCheckResp.FriendCheck> friendCheckList = req.getUidList().stream().map(friendId ->
                FriendCheckResp.FriendCheck.builder().uid(friendId).isFriend(myFriendUidSet.contains(friendId)).build()
        ).collect(Collectors.toList());
        return new FriendCheckResp(friendCheckList);
    }

    @Override
    public void applyAddFriend(Long uid, FriendApplyReq req) {
        // 判断是否是好友
        UserFriend userFriend = userFriendDao.getUserFriend(uid, req.getTargetUid());
        AssertUtil.isEmpty(userFriend, "已经是好友");
        // 判断是否已经发送过申请
        UserApply haveApply = userApplyDao.getUserApply(uid, req.getTargetUid());
        if (Objects.nonNull(haveApply)) {
            log.info("已有好友记录 uid:{} targetUid:{}", uid, req.getTargetUid());
            return;
        }
        // 判断要加的好友我有没有申请过 (别人请求自己)
        if (Objects.nonNull(userApplyDao.getUserApply(req.getTargetUid(), uid))) {
            // 直接同意好友请求
            ((UserFriendService) AopContext.currentProxy()).applyApprove(uid, FriendApproveReq.builder().applyId(req.getTargetUid()).build());
            return;
        }
        // 入库请求
        UserApply insert = UserApplyAdapter.buildUserApply(uid, req.getTargetUid(), req.getMsg());
        userApplyDao.save(insert);
        // 发送事件
        applicationEventPublisher.publishEvent(new UserApplyEvent(uid, insert));
    }

    @Transactional(rollbackFor = Exception.class)
    @RedissonLock(key = "#uid")
    @Override
    public void applyApprove(Long uid, FriendApproveReq req) {
        Long applyId = req.getApplyId();
        User user = userDao.getById(applyId);
        AssertUtil.notEqual(user.getStatus(), UserStatusEnum.BLACK.getStatus(), "对方已经被封禁, 无法添加好友");
        // 判断是否有请求
        UserApply userApply = userApplyDao.getUserApply(applyId, uid);
        AssertUtil.isNotEmpty(userApply, "没有好友申请");
        AssertUtil.equal(userApply.getTargetId(), uid, "不存在申请记录");
        AssertUtil.equal(userApply.getStatus(), ApplyStatusEnum.WAIT_APPROVAL.getCode(), "已同意好友申请");
        // 同意申请
        userApplyDao.approveFriendApply(userApply.getId());
        // 添加好友关系
        creatUserFriend(uid, applyId);
        // 创建房间
        roomFriendService.creatFriendRoom(uid, applyId);
    }


    /**
     * 创建好友关系（双向好友关系）
     *
     * @param targetUid targetUid 被申请人的 uid
     * @param applyId   申请人 uid
     */
    private void creatUserFriend(Long targetUid, Long applyId) {
        userFriendDao.saveBatch(ListUtil.of(
                UserFriendAdapter.buildUserFriend(targetUid, applyId),
                UserFriendAdapter.buildUserFriend(applyId, targetUid)
        ));
    }

    @Override
    public FriendUnreadResp unread(Long uid) {
        int unReadCount = userApplyDao.getUnReadCount(uid);
        return FriendUnreadResp.builder().unReadCount(unReadCount).build();
    }

    @Override
    public PageBaseResp<FriendApplyResp> getFriendApplyList(Long uid, PageBaseReq req) {
        Page<UserApply> page = userApplyDao.getFriendApplyListByPage(uid, req.getPageNo(), req.getPageSize());
        if (CollectionUtil.isEmpty(page.getRecords())) {
            return PageBaseResp.empty();
        }
        // 标记信息已读
        readFriendApply(uid, page.getRecords());
        return PageBaseResp.init((int) page.getCurrent(), (int) page.getSize(), page.getTotal(), FriendAdapter.buildFriendApplyPageResp(page.getRecords()));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteFriend(Long uid, FriendDeleteReq req) {
        // 1、判断是否是好友
        UserFriend userFriend = userFriendDao.getUserFriend(uid, req.getTargetUid());
        AssertUtil.isNotEmpty(userFriend, "您和ta还未添加好友");
        // 2、判断好友状态
        User user = userDao.getById(req.getTargetUid());
        AssertUtil.notEqual(user.getStatus(), UserStatusEnum.BLACK.getStatus(), "对方已经被封禁, 已经自动删除对方所有好友");
        // 3、删除好友
        userFriendDao.deleteFriend(uid, req.getTargetUid());
        // 4、删除房间
        // todo 删除房间
    }

    /**
     * 读取消息
     *
     * @param uid     当前用户也就是被申请人的 uid （UserApply 的 target_uid）
     * @param records 申请记录，申请人 uid
     */
    private void readFriendApply(Long uid, List<UserApply> records) {
        Set<Long> waitReadApplyUid = records.stream().filter(p -> ObjectUtil.equal(p.getReadStatus(), ApplyReadStatusEnum.UNREAD.getCode()))
                .map(UserApply::getUid).collect(Collectors.toSet());
        userApplyDao.readFriendApply(uid, waitReadApplyUid);
    }
}

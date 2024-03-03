package com.leikooo.mallchat.common.user.dao;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leikooo.mallchat.common.user.domain.entity.UserApply;
import com.leikooo.mallchat.common.user.domain.enums.ApplyReadStatusEnum;
import com.leikooo.mallchat.common.user.domain.enums.ApplyStatusEnum;
import com.leikooo.mallchat.common.user.domain.enums.ApplyTypeEnum;
import com.leikooo.mallchat.common.user.mapper.UserApplyMapper;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * <p>
 * 用户申请表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @since 2024-02-25
 */
@Service
public class UserApplyDao extends ServiceImpl<UserApplyMapper, UserApply> {

    /**
     * @param uid
     * @param targetUid
     */
    public UserApply getUserApply(Long uid, Long targetUid) {
        return lambdaQuery()
                .eq(UserApply::getUid, uid)
                .eq(UserApply::getTargetId, targetUid)
                .eq(UserApply::getStatus, ApplyStatusEnum.WAIT_APPROVAL.getCode())
                .eq(UserApply::getType, ApplyTypeEnum.ADD_FRIEND.getCode())
                .one();
    }

    public int getUnReadCount(Long uid) {
        return lambdaQuery()
                .eq(UserApply::getTargetId, uid)
                .count();
    }

    public Page<UserApply> getFriendApplyListByPage(Long uid, Integer pageNo, Integer pageSize) {
        return lambdaQuery()
                .eq(UserApply::getTargetId, uid)
                .eq(UserApply::getType, ApplyTypeEnum.ADD_FRIEND.getCode())
                .page(new Page<>(pageNo, pageSize));
    }

    /**
     * @param uid              就是 targetUid 因为自己是被申请的用户
     * @param waitReadApplyUid 申请人 uid 列表
     */
    public void readFriendApply(Long uid, Set<Long> waitReadApplyUid) {
        lambdaUpdate()
                .eq(UserApply::getTargetId, uid)
                .in(UserApply::getUid, waitReadApplyUid)
                .set(UserApply::getReadStatus, ApplyReadStatusEnum.READ.getCode())
                .update();
    }

    public void approveFriendApply(Long id) {
        lambdaUpdate()
                .eq(UserApply::getId, id)
                .set(UserApply::getStatus, ApplyStatusEnum.AGREE.getCode())
                .update();
    }
}

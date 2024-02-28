package com.leikooo.mallchat.common.user.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leikooo.mallchat.common.common.domain.enums.YesOrNoEnum;
import com.leikooo.mallchat.common.common.domain.vo.request.CursorPageBaseReq;
import com.leikooo.mallchat.common.common.domain.vo.response.CursorPageBaseResp;
import com.leikooo.mallchat.common.common.utils.CursorUtils;
import com.leikooo.mallchat.common.user.domain.entity.UserFriend;
import com.leikooo.mallchat.common.user.mapper.UserFriendMapper;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * <p>
 * 用户联系人表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @since 2024-02-25
 */
@Service
public class UserFriendDao extends ServiceImpl<UserFriendMapper, UserFriend> {
    public CursorPageBaseResp<UserFriend> getUserFriendList(Long uid, CursorPageBaseReq req) {
        return CursorUtils.getCursorPageByMysql(this, req, wrapper -> {
            wrapper.eq(UserFriend::getUid, uid);
        }, UserFriend::getId);
    }

    public List<UserFriend> getMyFriendList(Long uid, List<Long> uidList) {
        return lambdaQuery()
                .select(UserFriend::getUid, UserFriend::getFriendUid)
                .eq(UserFriend::getUid, uid)
                .in(UserFriend::getFriendUid, uidList)
                .list();
    }
}

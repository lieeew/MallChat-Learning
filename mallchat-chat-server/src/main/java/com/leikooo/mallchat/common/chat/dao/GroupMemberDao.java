package com.leikooo.mallchat.common.chat.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leikooo.mallchat.common.chat.domain.entity.GroupMember;
import com.leikooo.mallchat.common.chat.mapper.GroupMemberMapper;
;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 群成员表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @since 2024-03-02
 */
@Service
public class GroupMemberDao extends ServiceImpl<GroupMemberMapper, GroupMember> {

    public GroupMember getByGroupId(Long roomId, Long uid) {
        return lambdaQuery()
                .eq(GroupMember::getGroupId, roomId)
                .eq(GroupMember::getUid, uid)
                .one();
    }

    public List<GroupMember> getUsersByGroupId(Long groupId) {
        return lambdaQuery()
                .select(GroupMember::getUid)
                .eq(GroupMember::getGroupId, groupId)
                .list();
    }
}

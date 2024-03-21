package com.leikooo.mallchat.common.chat.service.cache;

import com.leikooo.mallchat.common.chat.dao.GroupMemberDao;
import com.leikooo.mallchat.common.chat.dao.RoomGroupDao;
import com.leikooo.mallchat.common.chat.domain.entity.GroupMember;
import com.leikooo.mallchat.common.chat.domain.entity.RoomGroup;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/3/19
 * @description
 */
@Component
public class RoomCache {
    @Resource
    private RoomGroupDao roomGroupDao;

    @Resource
    private GroupMemberDao groupMemberDao;

    @Cacheable(value = "member", key = "'groupMember' + #roomId")
    public List<Long> getRoomUsersId(Long roomId) {
        RoomGroup roomGroup = roomGroupDao.getById(roomId);
        if (Objects.isNull(roomGroup)) {
            return new ArrayList<>();
        }
        List<GroupMember> usersByGroupId = groupMemberDao.getUsersByGroupId(roomGroup.getId());
        return usersByGroupId.stream().map(GroupMember::getUid).collect(Collectors.toList());
    }

    @CacheEvict(value = "member", key = "'groupMember' + #roomId")
    public void evictRoomUsersId(Long roomId) {

    }
}

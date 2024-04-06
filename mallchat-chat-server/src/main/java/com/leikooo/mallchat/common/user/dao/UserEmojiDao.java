package com.leikooo.mallchat.common.user.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leikooo.mallchat.common.user.domain.entity.UserEmoji;
import com.leikooo.mallchat.common.user.mapper.UserEmojiMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户表情包 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/zongzibinbin">abin</a>
 * @since 2023-07-09
 */
@Service
public class UserEmojiDao extends ServiceImpl<UserEmojiMapper, UserEmoji> {

    public List<UserEmoji> listById(Long uid) {
        return lambdaQuery().eq(UserEmoji::getUid, uid).list();
    }

    public Integer countById(Long uid) {
        return lambdaQuery().eq(UserEmoji::getUid, uid).count();
    }
}

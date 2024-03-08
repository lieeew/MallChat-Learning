package com.leikooo.mallchat.common.user.dao;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/3/4
 * @description
 */
@SpringBootTest
public class UserFriendDaoTest {
    @Resource
    private UserFriendDao userFriendDao;

    @Test
    public void deleteFriend() {
        userFriendDao.deleteFriend(1L, 2L);
    }
}
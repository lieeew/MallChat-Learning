package com.leikooo.mallchat.common.user.adapter;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/3/2
 * @description
 */
public class FriendAdapterTest {
    @Test
    public void generateRoomKey() {
        FriendAdapter.generateRoomKey(1000L, 16L);
    }
}
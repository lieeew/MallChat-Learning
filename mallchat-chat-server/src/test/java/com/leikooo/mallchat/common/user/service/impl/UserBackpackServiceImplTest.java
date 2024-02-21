package com.leikooo.mallchat.common.user.service.impl;

import com.leikooo.mallchat.common.common.domain.enums.IdempotentEnum;
import com.leikooo.mallchat.common.user.domain.enums.ItemEnum;
import com.leikooo.mallchat.common.user.service.UserBackpackService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/2/19
 * @description
 */
@SpringBootTest
public class UserBackpackServiceImplTest {
    @Resource
    private UserBackpackService userBackpackService;

    @Test
    public void acquireItem() {
        userBackpackService.acquireItem(1L, ItemEnum.REG_TOP10_BADGE.getId(), IdempotentEnum.UID.getType(), "1");
    }
}
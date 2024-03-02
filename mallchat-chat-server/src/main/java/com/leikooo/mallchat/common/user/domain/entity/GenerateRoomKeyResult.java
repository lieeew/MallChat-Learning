package com.leikooo.mallchat.common.user.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/3/2
 * @description
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GenerateRoomKeyResult {
    private String roomKey;

    private List<Long> uidList;
}

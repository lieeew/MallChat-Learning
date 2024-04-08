package com.leikooo.mallchat.common.frequency.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 限流策略定义
 * @author liang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SlidingWindowDTO extends FrequencyControlDTO {

    /**
     * 窗口大小，默认 10 s
     */
    private int windowSize;

    /**
     * 窗口最小周期 1s (窗口大小是 10s， 1s一个小格子，-共10个格子)
     */
    private int period;
}

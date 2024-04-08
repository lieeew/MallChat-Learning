package com.leikooo.mallchat.common.frequency.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 限流策略定义
 * @author liang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FixedWindowDTO extends FrequencyControlDTO {

    /**
     * 频控时间范围，默认单位秒
     *
     * @return 时间范围
     */
    private Integer time;
}

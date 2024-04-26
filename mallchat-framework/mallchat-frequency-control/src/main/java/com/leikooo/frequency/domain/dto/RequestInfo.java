package com.leikooo.frequency.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * web请求信息收集类
 * @author liang
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestInfo {

    private Long uid;

    private String ip;
}

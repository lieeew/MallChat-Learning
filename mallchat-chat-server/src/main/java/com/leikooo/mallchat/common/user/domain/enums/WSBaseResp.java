package com.leikooo.mallchat.common.user.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

/**
 * Description: ws的基本返回信息体
 * @author  <a href="https://github.com/lieeew">leikooo</a>
 * Date: 2023-03-19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WSBaseResp<T> {
    /**
     * ws推送给前端的消息
     *
     * @see WSRespTypeEnum
     */
    private Integer type;
    private T data;
}

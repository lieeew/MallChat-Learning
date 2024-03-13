package com.leikooo.mallchat.common.chat.domain.vo.request;

import com.leikooo.mallchat.common.common.domain.vo.request.CursorPageBaseReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * @author : <a href="https://github.com/zongzibinbin">abin</a>
 * Date: 2023-07-17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageReadReq extends CursorPageBaseReq {
    @ApiModelProperty("消息id")
    @NotNull
    private Long msgId;

    @ApiModelProperty("查询类型 1已读 2未读")
    @NotNull
    private Long searchType;
}

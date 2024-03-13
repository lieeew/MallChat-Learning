package com.leikooo.mallchat.common.chat.domain.vo.request;

import com.leikooo.mallchat.common.common.domain.vo.request.CursorPageBaseReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * Description: 消息列表请求
 * @author : <a href="https://github.com/zongzibinbin">abin</a>
 * Date: 2023-03-29
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessagePageReq extends CursorPageBaseReq {
    @NotNull
    @ApiModelProperty("会话id")
    private Long roomId;
}

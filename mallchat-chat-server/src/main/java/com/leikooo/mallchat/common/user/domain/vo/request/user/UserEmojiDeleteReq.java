package com.leikooo.mallchat.common.user.domain.vo.request.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/4/6
 * @description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEmojiDeleteReq {
    @NotNull(message = "表情包 ID 不能为 null")
    @ApiModelProperty("表情包 ID ")
    private Long id;
}

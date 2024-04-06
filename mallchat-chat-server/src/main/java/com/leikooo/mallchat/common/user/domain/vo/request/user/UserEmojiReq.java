package com.leikooo.mallchat.common.user.domain.vo.request.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEmojiReq {
    /**
     * 表情地址
     */
    @NotEmpty(message = "表情包地址不能为空")
    @ApiModelProperty(value = "新增的表情url")
    private String expressionUrl;
}
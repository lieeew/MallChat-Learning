package com.leikooo.mallchat.common.user.domain.vo.request.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/2/16
 * @description
 */
@Data
public class ModifyNameReq {
    @ApiModelProperty("用户昵称")
    @NotNull
    @Length(max = 6, message = "昵称长度不能超过 6 个字符")
    private String name;

}

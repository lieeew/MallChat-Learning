package com.leikooo.mallchat.common.user.domain.vo.request.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/2/16
 * @description 拉黑用户请求
 */
@Data
public class BlockUserReq {
    @ApiModelProperty("UID")
    @NotNull
    private Integer uid;
}

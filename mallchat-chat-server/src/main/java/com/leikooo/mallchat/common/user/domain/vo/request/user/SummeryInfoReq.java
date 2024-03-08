package com.leikooo.mallchat.common.user.domain.vo.request.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/3/5
 * @description
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SummeryInfoReq {
    @ApiModelProperty(value = "用户信息入参")
    @NotNull
    @Size(max = 50)
    private List<infoReq> reqList;

    @Data
    public static class infoReq {
        @ApiModelProperty(value = "uid")
        private Long uid;

        @ApiModelProperty(value = "最近一次更新用户信息时间")
        private Long lastModifyTime;
    }
}

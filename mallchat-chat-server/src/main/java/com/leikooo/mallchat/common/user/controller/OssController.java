package com.leikooo.mallchat.common.user.controller;

import com.leikooo.mallchat.common.common.domain.vo.response.ApiResult;
import com.leikooo.mallchat.common.common.utils.RequestHolder;
import com.leikooo.mallchat.common.user.domain.vo.request.oss.UploadUrlReq;
import com.leikooo.mallchat.common.user.service.OssService;
import com.leikooo.mallchat.oss.domain.OssResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.junit.jupiter.api.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/4/5
 * @description
 */
@RestController
@RequestMapping("/capi/oss")
@Api(tags = "OSS 上传接口")
public class OssController {
    @Resource
    private OssService ossService;

    @GetMapping("/upload/url")
    @ApiOperation("获取临时上传链接")
    public ApiResult<OssResp> getUploadUrl(@Valid UploadUrlReq req) {
        return ApiResult.success(ossService.getUploadUrl(RequestHolder.get().getUid(), req));
    }
}

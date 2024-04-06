package com.leikooo.mallchat.common.user.service;

import com.leikooo.mallchat.common.user.domain.vo.request.oss.UploadUrlReq;
import com.leikooo.mallchat.oss.domain.OssResp;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/4/5
 * @description
 */
public interface OssService {
    OssResp getUploadUrl(Long uid, UploadUrlReq req);
}

package com.leikooo.mallchat.common.user.service.impl;

import com.leikooo.mallchat.common.common.utils.AssertUtil;
import com.leikooo.mallchat.common.user.domain.enums.OssSceneEnum;
import com.leikooo.mallchat.common.user.domain.vo.request.oss.UploadUrlReq;
import com.leikooo.mallchat.common.user.service.OssService;
import com.leikooo.mallchat.oss.MinIOTemplate;
import com.leikooo.mallchat.oss.domain.OssReq;
import com.leikooo.mallchat.oss.domain.OssResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/4/5
 * @description
 */
@Service
public class OssServiceImpl implements OssService {
    @Resource
    private MinIOTemplate minIOTemplate;

    @Override
    public OssResp getUploadUrl(Long uid, UploadUrlReq req) {
        OssSceneEnum ossScene = OssSceneEnum.of(req.getScene());
        AssertUtil.isNotEmpty(ossScene, "未知场景出现");
        OssReq ossReq = OssReq.builder().autoPath(true)
                .fileName(req.getFileName())
                .filePath(ossScene.getPath())
                .build();
        return minIOTemplate.getPreSignedObjectUrl(ossReq);
    }
}

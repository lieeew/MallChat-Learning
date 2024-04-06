package com.leikooo.mallchat.common.OSS;

import com.leikooo.mallchat.oss.MinIOTemplate;
import com.leikooo.mallchat.oss.domain.OssReq;
import com.leikooo.mallchat.oss.domain.OssResp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/4/5
 * @description
 */
@SpringBootTest
public class OssTest {

    @Autowired
    private MinIOTemplate minIOTemplate;

    @Test
    public void getUploadUrl() {
        OssReq ossReq = OssReq.builder()
                .fileName("test.jpg")
                .filePath("/test")
                .autoPath(false)
                .build();
        OssResp preSignedObjectUrl = minIOTemplate.getPreSignedObjectUrl(ossReq);
        System.out.println(preSignedObjectUrl);
    }
}

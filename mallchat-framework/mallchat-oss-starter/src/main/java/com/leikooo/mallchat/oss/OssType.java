package com.leikooo.mallchat.oss;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 */
@Getter
@AllArgsConstructor
public enum OssType {
    /**
     * Minio 对象存储
     */
    MINIO("minio", 1),

    /**
     * 华为 OBS
     */
    OBS("obs", 2),

    /**
     * 腾讯 COS
     */
    COS("tencent", 3),

    /**
     * 阿里巴巴 SSO
     */
    ALIBABA("alibaba", 4),
    ;

    /**
     * 名称
     */
    final String name;

    /**
     * 类型
     */
    final int type;

}
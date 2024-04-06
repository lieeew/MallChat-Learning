package com.leikooo.mallchat.oss;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 */
@Data
@AllArgsConstructor
public class OssFile {
    /**
     * OSS 存储时文件路径
     */
    String ossFilePath;

    /**
     * 原始文件名
     */
    String originalFileName;
}
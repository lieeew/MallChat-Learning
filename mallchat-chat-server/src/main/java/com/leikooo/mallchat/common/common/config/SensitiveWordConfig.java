package com.leikooo.mallchat.common.common.config;

import com.leikooo.mallchat.common.common.utils.sensitive.DFAFilter;
import com.leikooo.mallchat.common.common.utils.sensitive.SensitiveWordBs;
import com.leikooo.mallchat.common.sensitive.MyWordFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 */
@Configuration
public class SensitiveWordConfig {
    @Resource
    private MyWordFactory myWordFactory;

    /**
     * 初始化引导类
     */
    @Bean
    public SensitiveWordBs sensitiveWordBs() {
        return SensitiveWordBs.newInstance()
                .filterStrategy(DFAFilter.getInstance())
                .sensitiveWord(myWordFactory)
                .init();
    }
}
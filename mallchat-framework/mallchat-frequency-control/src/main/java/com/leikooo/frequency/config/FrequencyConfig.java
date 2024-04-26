package com.leikooo.frequency.config;

import com.leikooo.frequency.interceptor.GlobalRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/4/26
 * @description
 */
@Configuration
@Import({com.leikooo.frequency.aspect.FrequencyControlAspect.class})
public class FrequencyConfig {
    @Bean
    public GlobalRequestInterceptor globalRequestInterceptor() {
        return new GlobalRequestInterceptor();
    }

}

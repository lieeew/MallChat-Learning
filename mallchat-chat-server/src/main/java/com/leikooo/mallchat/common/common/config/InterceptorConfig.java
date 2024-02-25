package com.leikooo.mallchat.common.common.config;

import com.leikooo.mallchat.common.common.interceptor.BlackInterceptor;
import com.leikooo.mallchat.common.common.interceptor.CollectorInterceptor;
import com.leikooo.mallchat.common.common.interceptor.TokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/2/16
 * @description 拦截器配置，自定义拦截器生效必须要有这个配置
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Resource
    private TokenInterceptor tokenInterceptor;

    @Resource
    private CollectorInterceptor collectorInterceptor;

    @Resource
    private BlackInterceptor blackInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/capi/**");
        registry.addInterceptor(collectorInterceptor)
                .addPathPatterns("/capi/**");
        registry.addInterceptor(blackInterceptor)
                .addPathPatterns("/capi/**");
    }
}

package com.leikooo.mallchat.common;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author leikooo
 * @description 启动类
 */

@SpringBootApplication(scanBasePackages = {"com.leikooo.mallchat"})
@MapperScan({"com.leikooo.mallchat.common.**.mapper"})
@ServletComponentScan
@EnableConfigurationProperties
@ConfigurationPropertiesScan
@EnableAspectJAutoProxy(exposeProxy = true)
public class MallChatCustomApplication {
    public static void main(String[] args) {
        SpringApplication.run(MallChatCustomApplication.class, args);
    }
}

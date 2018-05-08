package com.cmazxiaoma;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;

/**
 * @author cmazxiaoma
 * @version V1.0
 * @Description: 将springBoot部署到外部Servlet容器里面
 * @date 2018/5/8 9:50
 */
@SpringBootApplication
@Configuration
public class WebApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        //设置启动类，用于独立tomcat运行的入口
        return builder.sources(WebApplication.class);
    }
}

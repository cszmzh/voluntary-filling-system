package com.banana.volunteer;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

// 打包war 增加war包启动类
public class WarStarterCatalinaApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        // 指向Application的SpringBoot启动类
        return builder.sources(ReportApplication.class);
    }
}

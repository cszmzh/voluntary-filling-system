package com.banana.volunteer.config;

import com.banana.volunteer.filter.XssAndSqlFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.DispatcherType;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class XssConfig {
    @Bean
    public FilterRegistrationBean xssFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.setFilter(new XssAndSqlFilter());
        registration.addUrlPatterns("/*");
        registration.setName("xssAndSqlFilter");
        registration.setOrder(Integer.MAX_VALUE);
        Map<String, String> initParameters = new HashMap();
        //-excludes用于配置不需要参数过滤的请求url;
        initParameters.put("excludes", "/favicon.ico,/img/*,/js/*,/css/*");
        //-isIncludeRichText默认为true，主要用于设置富文本内容是否需要过滤。
        initParameters.put("isIncludeRichText", "false");
        return registration;
    }
}

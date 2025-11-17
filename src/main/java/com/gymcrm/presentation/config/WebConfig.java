package com.gymcrm.presentation.config;

import com.gymcrm.presentation.interceptor.TransactionLoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Alish
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private TransactionLoggingInterceptor myInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(myInterceptor).addPathPatterns("/**");
    }
}

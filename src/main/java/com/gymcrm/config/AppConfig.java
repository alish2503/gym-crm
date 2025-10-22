package com.gymcrm.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Alish
 */
@EnableAspectJAutoProxy
@Configuration
@ComponentScan(basePackages = "com.gymcrm")
@PropertySource("classpath:application.properties")
public class AppConfig {
}

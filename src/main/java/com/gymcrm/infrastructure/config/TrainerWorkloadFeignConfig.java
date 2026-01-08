package com.gymcrm.infrastructure.config;

import com.gymcrm.infrastructure.security.service.port.JwtService;
import feign.RequestInterceptor;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TrainerWorkloadFeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor(JwtService jwtService) {
        return template -> {
            String token = jwtService.generateTokenForService();
            template.header("Authorization", "Bearer " + token);
            String transactionId = MDC.get("transactionId");
            if (transactionId != null) {
                template.header("X-Transaction-Id", transactionId);
            }
        };
    }
}

package com.gymcrm.aop;

import com.gymcrm.exception.EntityNotFoundException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Alish
 */
@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @AfterThrowing(
            pointcut = "execution(* com.gymcrm.service.impl.*.*(..)) || execution(* com.gymcrm.storage.*.*(..))",
            throwing = "ex"
    )
    public void handleServiceExceptions(JoinPoint joinPoint, Exception ex) {
        String serviceName = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        if (ex instanceof EntityNotFoundException) {
            log.warn("[{}] → {}(): {}", serviceName, methodName, ex.getMessage());
        } else {
            log.error("[{}] → {}() threw unexpected exception: {}", serviceName, methodName, ex.getMessage(), ex);
        }
    }
}

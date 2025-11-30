package com.gymcrm.infrastructure.metrics;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Alish
 */

@Aspect
@Component
public class MetricsAspect {
    private final CustomMetricsService customMetricsService;

    @Autowired
    public MetricsAspect(CustomMetricsService customMetricsService) {
        this.customMetricsService = customMetricsService;
    }

    @Pointcut("within(com.gymcrm.presentation.controller.impl.TrainingController)")
    public void trainingController() {}

    @Around("trainingController() && (execution(* getTrainingsForTrainee(..)) || execution(* getTrainingsForTrainer(..)))")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        String metricName = joinPoint.getSignature().getName();

        return customMetricsService.timeProcessing(metricName, () -> {
            try {
                return joinPoint.proceed();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        });
    }

    @AfterReturning("trainingController() && execution(* addTraining(..))")
    public void countAddTraining() {
        customMetricsService.recordTrainingCreated();
    }
}

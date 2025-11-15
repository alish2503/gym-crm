package com.gymcrm.presentation.aspect;

import com.gymcrm.application.request.CreateTraineeRequest;
import com.gymcrm.application.request.CreateTrainerRequest;
import com.gymcrm.application.request.CreateTrainingRequest;
import com.gymcrm.domain.model.Trainer;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Alish
 */
@Aspect
@Component
public class BusinessEventLoggingAspect {
    private static final Logger log = LoggerFactory.getLogger(BusinessEventLoggingAspect.class);

    @AfterReturning(
            pointcut = "execution(* com.gymcrm.application.service.impl.TraineeServiceImpl.createTrainee(..))",
            returning = "result"
    )
    public void logTraineeCreation(JoinPoint joinPoint, Object result) {
        CreateTraineeRequest req = (CreateTraineeRequest) joinPoint.getArgs()[0];
        log.info("Trainee created: {} {}", req.getFirstName(), req.getLastName());
    }

    @AfterReturning(
            pointcut = "execution(* com.gymcrm.application.service.impl.TrainerServiceImpl.createTrainer(..))",
            returning = "result"
    )
    public void logTrainerCreation(JoinPoint joinPoint, Object result) {
        CreateTrainerRequest request = (CreateTrainerRequest) joinPoint.getArgs()[0];
        log.info("Trainer created: {} {} {}", request.getFirstName(), request.getLastName(), request.getSpecialization());
    }

    @AfterReturning(
            pointcut = "execution(* com.gymcrm.application.service.impl.TrainingServiceImpl.createTraining(..))",
            returning = "result"
    )
    public void logTrainingCreation(JoinPoint joinPoint, Object result) {
        CreateTrainingRequest req = (CreateTrainingRequest) joinPoint.getArgs()[0];
        log.info("Training created: {} for trainee {} and trainer {} on {}",
                req.trainingName(),
                req.traineeUsername(),
                req.trainerUsername(),
                req.date());
    }

    @AfterReturning(
            pointcut = "execution(* com.gymcrm.application.service.impl.TraineeServiceImpl.updateTrainersForTrainee(..))",
            returning = "trainers"
    )
    public void logTraineeTrainerAssignment(JoinPoint joinPoint, Object trainers) {
        String traineeUsername = (String) joinPoint.getArgs()[0];
        List<Trainer> list = (List<Trainer>) trainers;
        String assigned = list.stream().map(t -> t.getUser().getUsername()).collect(Collectors.joining(", "));
        log.info("Trainee {} assigned to trainers: {}", traineeUsername, assigned);
    }

    @AfterReturning(
            pointcut = "execution(* com.gymcrm.application.service.impl.UserServiceImpl.toggle(..))"
    )
    public void logActivation(JoinPoint joinPoint) {
        String username = (String) joinPoint.getArgs()[0];
        boolean isActive = (Boolean) joinPoint.getArgs()[1];
        log.info("User {} set active={}", username, isActive);
    }

    @AfterReturning(
            pointcut = "execution(* com.gymcrm.application.service.impl.UserServiceImpl.changePassword(..))"
    )
    public void logPasswordChange(JoinPoint joinPoint) {
        String username = (String) joinPoint.getArgs()[0];
        log.info("Password changed for user: {}", username);
    }
}

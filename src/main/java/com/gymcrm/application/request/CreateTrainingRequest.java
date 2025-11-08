package com.gymcrm.application.request;

import com.gymcrm.domain.model.TrainingTypeEnum;

import java.time.LocalDate;

/**
 * @author Alish
 */
public record CreateTrainingRequest(
        String trainerUsername, String traineeUsername, TrainingTypeEnum type, String trainingName, LocalDate date, int duration) {}

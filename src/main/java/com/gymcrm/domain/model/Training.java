package com.gymcrm.domain.model;

import java.time.LocalDate;

/**
 * @param duration minutes
 * @author Alish
 */
public record Training(String trainingName, TrainingType type, LocalDate trainingDate, int duration, Trainer trainer,
                       Trainee trainee) {
}

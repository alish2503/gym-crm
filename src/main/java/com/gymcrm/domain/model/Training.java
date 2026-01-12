package com.gymcrm.domain.model;

import lombok.Getter;

import java.time.LocalDate;

/**
 * @author Alish
 */
@Getter
public class Training {
    private Long traineeId;
    private Long trainerId;
    private final TrainingType type;
    private final String name;
    private final LocalDate date;
    int durationInHours;
    Trainer trainer;
    Trainee trainee;

    public Training(TrainingType type, String name, LocalDate date, int durationInHours, Trainer trainer, Trainee trainee) {
        this.type = type;
        this.name = name;
        this.date = date;
        this.durationInHours = durationInHours;
        this.trainer = trainer;
        this.trainee = trainee;
    }

    public Training(TrainingType type, String name, LocalDate date, int durationInHours, Trainer trainer) {
        this.type = type;
        this.name = name;
        this.date = date;
        this.durationInHours = durationInHours;
        this.trainer = trainer;
    }

    public Training(TrainingType type, String name, LocalDate date, int durationInHours, Trainee trainee) {
        this.type = type;
        this.name = name;
        this.date = date;
        this.durationInHours = durationInHours;
        this.trainee = trainee;
    }

    public Training(TrainingType type, String name, LocalDate date, int durationInHours, Long trainerId, Long traineeId) {
        this.traineeId = traineeId;
        this.trainerId = trainerId;
        this.type = type;
        this.name = name;
        this.date = date;
        this.durationInHours = durationInHours;
    }

}

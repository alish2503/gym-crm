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
    int duration;
    Trainer trainer;
    Trainee trainee;

    public Training(TrainingType type, String name, LocalDate date, int duration, Trainer trainer, Trainee trainee) {
        this.type = type;
        this.name = name;
        this.date = date;
        this.duration = duration;
        this.trainer = trainer;
        this.trainee = trainee;
    }

    public Training(TrainingType type, String name, LocalDate date, int duration, Trainer trainer) {
        this.type = type;
        this.name = name;
        this.date = date;
        this.duration = duration;
        this.trainer = trainer;
    }

    public Training(TrainingType type, String name, LocalDate date, int duration, Trainee trainee) {
        this.type = type;
        this.name = name;
        this.date = date;
        this.duration = duration;
        this.trainee = trainee;
    }

    public Training(TrainingType type, String name, LocalDate date, int duration, Long trainerId, Long traineeId) {
        this.traineeId = traineeId;
        this.trainerId = trainerId;
        this.type = type;
        this.name = name;
        this.date = date;
        this.duration = duration;
    }

}

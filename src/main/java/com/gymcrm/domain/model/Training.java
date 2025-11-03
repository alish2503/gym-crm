package com.gymcrm.domain.model;

import java.time.LocalDate;

/**
 * @author Alish
 */
public class Training {
    private TrainingType type;
    private String name;
    private LocalDate date;
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

    public TrainingType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getDuration() {
        return duration;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public Trainee getTrainee() {
        return trainee;
    }
}

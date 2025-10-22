package com.gymcrm.model;

import java.time.LocalDate;

/**
 * @author Alish
 */
public class Training {
    private Long id;
    private String trainingName;
    private TrainingType type;
    private LocalDate trainingDate;
    private int duration; // minutes
    private Trainer trainer;
    private Trainee trainee;

    public Training(String trainingName, TrainingType type, LocalDate trainingDate, int duration, Trainer trainer, Trainee trainee) {
        this.trainingName = trainingName;
        this.type = type;
        this.trainingDate = trainingDate;
        this.duration = duration;
        this.trainer = trainer;
        this.trainee = trainee;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public Trainee getTrainee() {
        return trainee;
    }

    @Override
    public String toString() {
        return String.format("Training{id=%d, name='%s', trainee='%s', trainer='%s'}",
                id, trainingName,
                trainee.getUsername(),
                trainer.getUsername());
    }
}

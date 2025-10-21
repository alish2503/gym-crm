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

    public Long getId() {
        return id;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public TrainingType getType() {
        return type;
    }

    public LocalDate getTrainingDate() {
        return trainingDate;
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public void setType(TrainingType type) {
        this.type = type;
    }

    public void setTrainingDate(LocalDate trainingDate) {
        this.trainingDate = trainingDate;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public void setTrainee(Trainee trainee) {
        this.trainee = trainee;
    }
}

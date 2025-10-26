package com.gymcrm.infrastructure.persistence.dao;

import java.time.LocalDate;

/**
 * @author Alish
 */
public class TrainingDao {
    private Long id;
    private final String traineeUsername;
    private final String trainerUsername;
    private final String trainingTypeName;
    private final String trainingName;
    private final LocalDate trainingDate;
    private final int duration; // minutes

    public TrainingDao(String traineeUsername, String trainerUsername, String trainingTypeName, String trainingName, LocalDate trainingDate, int duration) {
        this.traineeUsername = traineeUsername;
        this.trainerUsername = trainerUsername;
        this.trainingTypeName = trainingTypeName;
        this.trainingName = trainingName;
        this.trainingDate = trainingDate;
        this.duration = duration;
    }

    public Long getId() {
        return id;
    }

    public String getTraineeUsername() {
        return traineeUsername;
    }

    public String getTrainerUsername() {
        return trainerUsername;
    }

    public String getTrainingTypeName() {
        return trainingTypeName;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public LocalDate getTrainingDate() {
        return trainingDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

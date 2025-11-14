package com.gymcrm.presentation.dto.request;

import com.gymcrm.presentation.validation.ValidTrainingType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * @author Alish
 */
public class CreateTrainingDto {

    @NotBlank(message = "Trainer username cannot be blank")
    @Size(max = 50)
    private String trainerUsername;

    @NotBlank(message = "Trainee username cannot be blank")
    @Size(max = 50)
    private String traineeUsername;

    @NotBlank(message = "Training type cannot be blank")
    @ValidTrainingType
    @Size(max = 10)
    private String type;

    @NotBlank(message = "Training name cannot be blank")
    @Size(max = 50)
    private String trainingName;

    @NotNull(message = "Training date cannot be blank")
    @Future
    private LocalDate date;

    @Positive
    private int duration;

    public CreateTrainingDto() {}

    public String getTrainerUsername() {
        return trainerUsername;
    }

    public String getTraineeUsername() {
        return traineeUsername;
    }

    public String getType() {
        return type;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getDuration() {
        return duration;
    }

    public void setTrainerUsername(String trainerUsername) {
        this.trainerUsername = trainerUsername;
    }

    public void setTraineeUsername(String traineeUsername) {
        this.traineeUsername = traineeUsername;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}

package com.gymcrm.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gymcrm.presentation.validation.ValidTrainingType;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(example = "Mike.Black")
    @NotBlank(message = "Trainer username cannot be blank")
    @Size(max = 50)
    private String trainerUsername;

    @Schema(example = "John.Doe")
    @NotBlank(message = "Trainee username cannot be blank")
    @Size(max = 50)
    private String traineeUsername;

    @Schema(example = "FITNESS")
    @NotBlank(message = "Training type cannot be blank")
    @ValidTrainingType
    @Size(max = 10)
    private String trainingType;

    @Schema(example = "Morning Yoga")
    @NotBlank(message = "Training name cannot be blank")
    @Size(max = 50)
    private String trainingName;

    @Schema(example = "2026-10-10")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "Training date cannot be blank")
    @Future(message = "Date must be in the future")
    private LocalDate date;

    @Schema(example = "80")
    @Positive(message = "Duration must be positive")
    private int duration;

    public CreateTrainingDto() {}

    public String getTrainerUsername() {
        return trainerUsername;
    }

    public String getTraineeUsername() {
        return traineeUsername;
    }

    public String getTrainingType() {
        return trainingType;
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

    public void setTrainingType(String trainingType) {
        this.trainingType = trainingType.toUpperCase();
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

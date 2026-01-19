package com.gymcrm.presentation.dto.request;

import com.gymcrm.domain.model.TrainingTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * @author Alish
 */
@Setter
@Getter
@NoArgsConstructor
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
    @NotNull(message = "Training type cannot be blank")
    private TrainingTypeEnum trainingType;

    @Schema(example = "Morning Yoga")
    @NotBlank(message = "Training name cannot be blank")
    @Size(max = 50)
    private String trainingName;

    @Schema(example = "2026-10-10")
    @NotNull(message = "Training date cannot be blank")
    @Future(message = "Date must be in the future")
    private LocalDate date;

    @Schema(example = "2")
    @Positive(message = "Duration must be positive")
    private Integer durationInHours;
}

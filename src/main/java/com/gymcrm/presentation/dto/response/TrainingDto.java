package com.gymcrm.presentation.dto.response;

import com.gymcrm.presentation.dto.FullNameDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

/**
 * @author Alish
 */
public class TrainingDto {

    @Schema(example = "Morning Yoga")
    private final String trainingName;

    @Schema(example = "2025-10-10")
    private final LocalDate date;

    @Schema(example = "FITNESS")
    private final String type;

    @Schema(example = "80")
    private final int duration;

    public TrainingDto(String trainingName, LocalDate date, String type, int duration) {
        this.trainingName = trainingName;
        this.date = date;
        this.type = type;
        this.duration = duration;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public int getDuration() {
        return duration;
    }
}

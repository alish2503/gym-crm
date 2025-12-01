package com.gymcrm.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

/**
 * @author Alish
 */
@Getter
@AllArgsConstructor
public class TrainingDto {

    @Schema(example = "Morning Yoga")
    private final String trainingName;

    @Schema(example = "2025-10-10")
    private final LocalDate date;

    @Schema(example = "FITNESS")
    private final String type;

    @Schema(example = "80")
    private final int duration;
}

package com.gymcrm.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class TrainingFilterDto {

    @Schema(description = "Date of the beginning in the format: YYYY-MM-DD")
    private LocalDate from;

    @Schema(description = "Date of the ending in the format: YYYY-MM-DD")
    private LocalDate to;
}

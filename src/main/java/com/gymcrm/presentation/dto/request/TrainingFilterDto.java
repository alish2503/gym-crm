package com.gymcrm.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

/**
 * @author Alish
 */
public class TrainingFilterDto {

    @Schema(description = "Date of the beginning in the format: YYYY-MM-DD")
    private LocalDate from;

    @Schema(description = "Date of the ending in the format: YYYY-MM-DD")
    private LocalDate to;

    public TrainingFilterDto() {}

    public LocalDate getFrom() {
        return from;
    }

    public LocalDate getTo() {
        return to;
    }

    public void setFrom(LocalDate from) {
        this.from = from;
    }

    public void setTo(LocalDate to) {
        this.to = to;
    }
}

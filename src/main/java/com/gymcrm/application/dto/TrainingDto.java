package com.gymcrm.application.dto;

import com.gymcrm.domain.model.TrainingTypeEnum;

import java.time.LocalDate;

/**
 * @author Alish
 */
public class TrainingDto {
    String trainingName;
    LocalDate date;
    TrainingTypeEnum type;
    int duration;

    public TrainingDto(String trainingName, LocalDate date, TrainingTypeEnum type, int duration) {
        this.trainingName = trainingName;
        this.date = date;
        this.type = type;
        this.duration = duration;
    }

    public TrainingDto() {}
}

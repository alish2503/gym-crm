package com.gymcrm.application.dto.response;

import com.gymcrm.application.dto.TrainingDto;
import com.gymcrm.domain.model.TrainingTypeEnum;

import java.time.LocalDate;

/**
 * @author Alish
 */
public class TrainingForTrainerResponse extends TrainingDto {
    String traineeName;

    public TrainingForTrainerResponse(String trainingName, LocalDate date, TrainingTypeEnum type,
                                      int duration, String traineeName)
    {
        super(trainingName, date, type, duration);
        this.traineeName = traineeName;
    }
}

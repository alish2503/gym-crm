package com.gymcrm.presentation.dto.response;

import com.gymcrm.presentation.dto.FullNameDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

/**
 * @author Alish
 */
public class TrainingForTrainerDto extends TrainingDto {

    private final FullNameDto traineeName;

    public TrainingForTrainerDto(String trainingName, LocalDate date, String type, int duration,
                                 FullNameDto traineeName)
    {
        super(trainingName, date, type, duration);
        this.traineeName = traineeName;
    }

    public FullNameDto getTraineeName() {
        return traineeName;
    }
}

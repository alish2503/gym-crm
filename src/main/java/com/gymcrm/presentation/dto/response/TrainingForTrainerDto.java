package com.gymcrm.presentation.dto.response;

import com.gymcrm.presentation.dto.FullNameDto;
import lombok.Getter;

import java.time.LocalDate;

/**
 * @author Alish
 */
@Getter
public class TrainingForTrainerDto extends TrainingDto {

    private final FullNameDto traineeName;

    public TrainingForTrainerDto(String trainingName, LocalDate date, String type, int duration,
                                 FullNameDto traineeName)
    {
        super(trainingName, date, type, duration);
        this.traineeName = traineeName;
    }
}

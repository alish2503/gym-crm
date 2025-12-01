package com.gymcrm.presentation.dto.response;

import com.gymcrm.presentation.dto.FullNameDto;
import lombok.Getter;

import java.time.LocalDate;

/**
 * @author Alish
 */
@Getter
public class TrainingForTraineeDto extends TrainingDto {

    private final FullNameDto trainerName;

    public TrainingForTraineeDto(String trainingName, LocalDate date, String type, int duration,
                                 FullNameDto trainerName)
    {
        super(trainingName, date, type, duration);
        this.trainerName = trainerName;
    }
}

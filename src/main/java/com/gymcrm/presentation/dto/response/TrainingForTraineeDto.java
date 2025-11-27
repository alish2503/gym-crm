package com.gymcrm.presentation.dto.response;

import com.gymcrm.presentation.dto.FullNameDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

/**
 * @author Alish
 */
public class TrainingForTraineeDto extends TrainingDto {

    private final FullNameDto trainerName;

    public TrainingForTraineeDto(String trainingName, LocalDate date, String type, int duration,
                                 FullNameDto trainerName)
    {
        super(trainingName, date, type, duration);
        this.trainerName = trainerName;
    }

    public FullNameDto getTrainerName() {
        return trainerName;
    }
}

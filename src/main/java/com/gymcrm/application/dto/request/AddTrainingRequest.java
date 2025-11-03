package com.gymcrm.application.dto.request;

import com.gymcrm.application.dto.TrainingDto;
import com.gymcrm.domain.model.TrainingTypeEnum;

import java.time.LocalDate;

/**
 * @author Alish
 */
public class AddTrainingRequest extends TrainingDto {
    String traineeUserName;
    String trainerUserName;

    public void setTraineeUserName(String traineeUserName) {
        this.traineeUserName = traineeUserName;
    }

    public void setTrainerUserName(String trainerUserName) {
        this.trainerUserName = trainerUserName;
    }
}

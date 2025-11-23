package com.gymcrm.presentation.dto.request;

import com.gymcrm.domain.model.TrainingTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

/**
 * @author Alish
 */
public class TrainingFilterForTraineeDto extends TrainingFilterDto {

    @Schema(description= "Example: John Doe")
    @Size(max = 50)
    private String trainerName;

    private TrainingTypeEnum type;

    public TrainingFilterForTraineeDto() {}

    public String getTrainerName() {
        return trainerName;
    }

    public TrainingTypeEnum getType() {
        return type;
    }

    public void setType(TrainingTypeEnum type) {
        this.type = type;
    }

    public void setTrainerName(String trainerName) {
        this.trainerName = trainerName;
    }
}

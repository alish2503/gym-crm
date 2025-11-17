package com.gymcrm.presentation.dto.request;

import com.gymcrm.presentation.validation.ValidTrainingType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

/**
 * @author Alish
 */
public class TrainingFilterForTraineeDto extends TrainingFilterDto {

    @Schema(example= "John Doe")
    @Size(max = 50)
    private String trainerName;

    @ValidTrainingType
    private String type;

    public TrainingFilterForTraineeDto() {}

    public String getTrainerName() {
        return trainerName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type.toUpperCase();
    }

    public void setTrainerName(String trainerName) {
        this.trainerName = trainerName;
    }
}

package com.gymcrm.presentation.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

/**
 * @author Alish
 */
public class TrainingFilterForTrainerDto extends TrainingFilterDto {

    @Schema(description = "Example: John Doe")
    @Size(max = 50)
    private String traineeName;

    public TrainingFilterForTrainerDto() {}

    public String getTraineeName() {
        return traineeName;
    }

    public void setTraineeName(String traineeName) {
        this.traineeName = traineeName;
    }
}

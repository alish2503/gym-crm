package com.gymcrm.presentation.dto.request;

import com.gymcrm.domain.model.TrainingTypeEnum;
import com.gymcrm.presentation.dto.FullNameDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * @author Alish
 */
public class UpdateTrainerDto extends FullNameDto {

    @Schema(example = "false")
    @NotNull(message = "Activity cannot be null")
    private Boolean isActive;

    @Schema(example = "FITNESS")
    @NotNull(message = "Trainer specialization cannot be blank")
    private TrainingTypeEnum specialization;

    public UpdateTrainerDto(String firstName, String lastName, boolean isActive,
                            TrainingTypeEnum specialization)
    {
        super(firstName, lastName);
        this.isActive = isActive;
        this.specialization = specialization;
    }

    public UpdateTrainerDto() {}

    public boolean isActive() {
        return isActive;
    }

    public TrainingTypeEnum getSpecialization() {
        return specialization;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setSpecialization(TrainingTypeEnum specialization) {
        this.specialization = specialization;
    }
}

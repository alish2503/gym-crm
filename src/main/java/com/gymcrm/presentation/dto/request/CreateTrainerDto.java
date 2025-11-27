package com.gymcrm.presentation.dto.request;

import com.gymcrm.domain.model.TrainingTypeEnum;
import com.gymcrm.presentation.dto.FullNameDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * @author Alish
 */
public class CreateTrainerDto extends FullNameDto {

    @Schema(example = "FITNESS")
    @NotNull(message = "Trainer specialization cannot be blank")
    private TrainingTypeEnum specialization;

    public CreateTrainerDto(String firstName, String lastName, TrainingTypeEnum specialization)
    {
        super(firstName, lastName);
        this.specialization = specialization;
    }

    public CreateTrainerDto() {}

    public TrainingTypeEnum getSpecialization() {
        return specialization;
    }

    public void setSpecialization(TrainingTypeEnum specialization) {
        this.specialization = specialization;
    }
}

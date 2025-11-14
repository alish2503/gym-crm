package com.gymcrm.presentation.dto.request;

import com.gymcrm.presentation.dto.FullNameDto;
import com.gymcrm.presentation.validation.ValidTrainingType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * @author Alish
 */
public class CreateTrainerDto extends FullNameDto {

    @NotBlank(message = "Trainer specialization cannot be blank")
    @ValidTrainingType
    @Size(max = 10)
    private String specialization;

    public CreateTrainerDto(String firstName, String lastName, String specialization)
    {
        super(firstName, lastName);
        this.specialization = specialization;
    }

    public CreateTrainerDto() {}

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}

package com.gymcrm.presentation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * @author Alish
 */
public class UpdateTrainersDto {

    @NotEmpty(message = "Trainer list cannot be empty")
    @Size(max = 50, message = "Cannot have more than 50 trainers")
    @Valid
    private List<@NotBlank(message = "Trainer username cannot be blank") String> trainerUsernames;

    public UpdateTrainersDto() {}

    public List<String> getTrainerUsernames() {
        return trainerUsernames;
    }

    public void setTrainerUsernames(List<String> trainerUsernames) {
        this.trainerUsernames = trainerUsernames;
    }
}

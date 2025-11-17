package com.gymcrm.presentation.dto.request;

import com.gymcrm.presentation.dto.response.TraineeDto;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * @author Alish
 */
public class UpdateTrainersDto {

    @ArraySchema(schema = @Schema(example = "John.Doe"))
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

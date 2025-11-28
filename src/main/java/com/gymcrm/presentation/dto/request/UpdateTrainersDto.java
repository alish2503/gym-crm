package com.gymcrm.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author Alish
 */
@Setter
@Getter
@NoArgsConstructor
public class UpdateTrainersDto {

    @ArraySchema(schema = @Schema(example = "John.Doe"))
    @NotEmpty(message = "Trainer list cannot be empty")
    @Size(max = 50, message = "Cannot have more than 50 trainers")
    @Valid
    private List<@NotBlank(message = "Trainer username cannot be blank") String> trainerUsernames;
}

package com.gymcrm.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Alish
 */
@JsonPropertyOrder({
        "firstName",
        "lastName",
        "dateOfBirth",
        "address",
        "isActive",
        "trainers"
})
public class TraineeWithTrainersDto extends UserDto {

    @Schema(example = "1998-03-15")
    private final LocalDate dateOfBirth;

    @Schema(example = "\"123 Main St, London\"")
    private final String address;

    @ArraySchema(schema = @Schema(implementation = TrainerDto.class))
    @JsonProperty("trainers")
    private final List<TrainerDto> trainerDtos;

    public TraineeWithTrainersDto(boolean isActive, String firstName, String lastName, LocalDate dateOfBirth,
                                  String address, List<TrainerDto> trainerDtos)
    {
        super(isActive, firstName, lastName);
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.trainerDtos = trainerDtos;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public List<TrainerDto> getTrainerDtos() {
        return trainerDtos;
    }
}

package com.gymcrm.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Alish
 */

@JsonPropertyOrder({
        "username",
        "firstName",
        "lastName",
        "dateOfBirth",
        "address",
        "isActive",
        "trainers"
})
public class TraineeWithTrainersAfterUpdateDto extends TraineeWithTrainersDto {

    @Schema(example = "John.Doe")
    private final String username;

    public TraineeWithTrainersAfterUpdateDto(boolean isActive, String firstName, String lastName,
                                             LocalDate dateOfBirth, String address,
                                             List<TrainerDto> trainerDtos, String username)
    {
        super(isActive, firstName, lastName, dateOfBirth, address, trainerDtos);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}

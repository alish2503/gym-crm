package com.gymcrm.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

/**
 * @author Alish
 */
@Getter
@JsonPropertyOrder({
        "firstName",
        "lastName",
        "specialization",
        "isActive",
        "trainees"
})
public class TrainerWithTraineesDto extends UserDto {

    @Schema(example = "FITNESS")
    private final String specialization;

    @ArraySchema(schema = @Schema(implementation = TraineeDto.class))
    @JsonProperty("trainees")
    private final List<TraineeDto> traineeDtos;

    public TrainerWithTraineesDto(boolean isActive, String firstName, String lastName, String specialization,
                                  List<TraineeDto> traineeDtos)
    {
        super(isActive, firstName, lastName);
        this.specialization = specialization;
        this.traineeDtos = traineeDtos;
    }
}

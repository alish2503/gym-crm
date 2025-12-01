package com.gymcrm.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

/**
 * @author Alish
 */

@Getter
@JsonPropertyOrder({
        "username",
        "firstName",
        "lastName",
        "specialization",
        "isActive",
        "trainees"
})
public class TrainerWithTraineesAfterUpdateDto extends TrainerWithTraineesDto {

    @Schema(example = "John.Doe")
    private final String username;

    public TrainerWithTraineesAfterUpdateDto(boolean isActive, String firstName, String lastName,
                                             String specialization, List<TraineeDto> traineeDtos,
                                             String username)
    {
        super(isActive, firstName, lastName, specialization, traineeDtos);
        this.username = username;
    }
}

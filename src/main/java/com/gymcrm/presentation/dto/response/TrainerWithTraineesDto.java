package com.gymcrm.presentation.dto.response;

import java.util.List;

/**
 * @author Alish
 */
public class TrainerWithTraineesDto extends UserDto {
    private String specialization;
    private List<TraineeDto> traineeDtos;

    public TrainerWithTraineesDto(boolean isActive, String firstName, String lastName, String specialization,
                                  List<TraineeDto> traineeDtos)
    {
        super(isActive, firstName, lastName);
        this.specialization = specialization;
        this.traineeDtos = traineeDtos;
    }
}

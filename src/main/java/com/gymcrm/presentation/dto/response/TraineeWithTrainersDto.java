package com.gymcrm.presentation.dto.response;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Alish
 */
public class TraineeWithTrainersDto extends UserDto {
    private final LocalDate dateOfBirth;
    private final String address;
    private List<TrainerDto> trainerDtos;

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
}

package com.gymcrm.presentation.mapper;

import com.gymcrm.application.request.CreateTraineeRequest;
import com.gymcrm.application.request.UpdateTraineeRequest;
import com.gymcrm.domain.model.Trainee;
import com.gymcrm.domain.model.User;
import com.gymcrm.presentation.dto.request.CreateTraineeDto;
import com.gymcrm.presentation.dto.request.UpdateTraineeDto;
import com.gymcrm.presentation.dto.response.TraineeDto;
import com.gymcrm.presentation.dto.response.TraineeWithTrainersDto;
import com.gymcrm.presentation.dto.response.TrainerDto;

import java.util.List;

/**
 * @author Alish
 */
public class TraineeDtoMapper {

    private TraineeDtoMapper() {}

    public static CreateTraineeRequest toDomain(CreateTraineeDto dto) {
        return new CreateTraineeRequest(dto.getFirstName(), dto.getLastName(), dto.getDateOfBirth(),
                dto.getAddress());
    }

    public static UpdateTraineeRequest toDomain(String username, UpdateTraineeDto dto) {
        return new UpdateTraineeRequest(username, dto.getFirstName(), dto.getLastName(),
                dto.isActive(), dto.getDateOfBirth(), dto.getAddress()
        );
    }

    public static TraineeDto toDto(Trainee trainee) {
        User userProfile = trainee.getUser();
        return new TraineeDto(userProfile.getUsername(), userProfile.getFirstName(), userProfile.getLastName());
    }

    public static TraineeWithTrainersDto toDtoWithTrainers(Trainee trainee) {
        List<TrainerDto> trainerDtos = trainee.getTrainers().stream().map(TrainerDtoMapper::toDto).toList();
        User traineeProfile = trainee.getUser();
        return new TraineeWithTrainersDto(traineeProfile.isActive(), traineeProfile.getFirstName(),
                traineeProfile.getLastName(), trainee.getDateOfBirth(), trainee.getAddress(), trainerDtos);
    }
}

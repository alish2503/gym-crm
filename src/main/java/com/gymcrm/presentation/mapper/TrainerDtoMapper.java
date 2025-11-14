package com.gymcrm.presentation.mapper;

import com.gymcrm.application.request.CreateTrainerRequest;
import com.gymcrm.application.request.UpdateUserRequest;
import com.gymcrm.domain.model.Trainer;
import com.gymcrm.domain.model.TrainingTypeEnum;
import com.gymcrm.domain.model.User;
import com.gymcrm.presentation.dto.request.CreateTrainerDto;
import com.gymcrm.presentation.dto.request.UpdateUserDto;
import com.gymcrm.presentation.dto.response.TrainerDto;
import com.gymcrm.presentation.dto.response.TrainerWithTraineesDto;
import com.gymcrm.presentation.dto.response.TraineeDto;

import java.util.List;

/**
 * @author Alish
 */
public class TrainerDtoMapper {

    private TrainerDtoMapper() {}

    public static CreateTrainerRequest toDomain(CreateTrainerDto dto) {
        TrainingTypeEnum typeEnum = TrainingTypeEnum.valueOf(dto.getSpecialization());
        return new CreateTrainerRequest(dto.getFirstName(), dto.getLastName(), typeEnum);
    }

    public static UpdateUserRequest toDomain(String username, UpdateUserDto dto) {
        return new UpdateUserRequest(username, dto.getFirstName(), dto.getLastName(), dto.isActive());
    }

    public static TrainerDto toDto(Trainer trainer) {
        User userProfile = trainer.getUser();
        String specialization = trainer.getSpecialization().typeEnum().name();
        return new TrainerDto(userProfile.getUsername(), userProfile.getFirstName(),
                userProfile.getLastName(), specialization);
    }

    public static TrainerWithTraineesDto toDtoWithTrainees(Trainer trainer) {
        List<TraineeDto> traineeDtos = trainer.getTrainees().stream().map(TraineeDtoMapper::toDto).toList();
        User trainerProfile = trainer.getUser();
        String specialization = trainer.getSpecialization().typeEnum().name();
        return new TrainerWithTraineesDto(trainerProfile.isActive(), trainerProfile.getFirstName(),
                trainerProfile.getLastName(), specialization, traineeDtos);
    }
}

package com.gymcrm.presentation.mapper;

import com.gymcrm.application.request.CreateTrainingRequest;
import com.gymcrm.domain.model.FullName;
import com.gymcrm.domain.model.Training;
import com.gymcrm.domain.model.TrainingFilter;
import com.gymcrm.domain.model.TrainingType;
import com.gymcrm.domain.model.TrainingTypeEnum;
import com.gymcrm.domain.model.User;
import com.gymcrm.presentation.dto.FullNameDto;
import com.gymcrm.presentation.dto.request.CreateTrainingDto;
import com.gymcrm.presentation.dto.request.TrainingFilterDto;
import com.gymcrm.presentation.dto.response.TrainingDto;
import com.gymcrm.presentation.dto.response.TrainingTypeDto;

import java.util.Optional;

/**
 * @author Alish
 */
public class TrainingDtoMapper {

    private TrainingDtoMapper() {}

    public static CreateTrainingRequest toDomain(CreateTrainingDto dto) {
        TrainingTypeEnum typeEnum = TrainingTypeEnum.valueOf(dto.getType());
        return new CreateTrainingRequest(dto.getTrainerUsername(), dto.getTraineeUsername(), typeEnum,
                dto.getTrainingName(), dto.getDate(), dto.getDuration());
    }

    public static TrainingFilter toDomain(TrainingFilterDto dto) {
        TrainingTypeEnum typeEnum = Optional.ofNullable(dto.getType())
                .map(String::toUpperCase)
                .map(TrainingTypeEnum::valueOf)
                .orElse(null);

        FullName fullName = Optional.ofNullable(dto.getPersonName())
                .filter(name -> !name.isBlank())
                .map(name -> {
                    String[] parts = name.trim().split("\\s+", 2);
                    String firstName = parts.length > 0 ? parts[0] : "";
                    String lastName = parts.length > 1 ? parts[1] : "";
                    return new FullName(firstName, lastName);
                })
                .orElse(null);

        return new TrainingFilter(dto.getFrom(), dto.getTo(), fullName, typeEnum);
    }

    public static TrainingTypeDto toDto(TrainingType trainingType) {
        return new TrainingTypeDto(trainingType.id(), trainingType.typeEnum().name());
    }

    public static TrainingDto toDtoForTrainee(Training training) {
        String type = training.getType().typeEnum().name();
        User userProfile = training.getTrainer().getUser();
        FullNameDto fullNameDto = new FullNameDto(userProfile.getFirstName(), userProfile.getLastName());
        return new TrainingDto(training.getName(), training.getDate(), type, training.getDuration(),
                fullNameDto);
    }

    public static TrainingDto toDtoForTrainer(Training training) {
        String type = training.getType().typeEnum().name();
        User userProfile = training.getTrainee().getUser();
        FullNameDto fullNameDto = new FullNameDto(userProfile.getFirstName(), userProfile.getLastName());
        return new TrainingDto(training.getName(), training.getDate(), type, training.getDuration(),
                fullNameDto);
    }
}

package com.gymcrm.presentation.controller.impl;

import com.gymcrm.application.response.UserCredentials;
import com.gymcrm.application.service.port.TraineeService;
import com.gymcrm.domain.model.Trainee;
import com.gymcrm.domain.model.Trainer;
import com.gymcrm.presentation.controller.port.TraineeControllerApi;
import com.gymcrm.presentation.dto.request.CreateTraineeDto;
import com.gymcrm.presentation.dto.request.UpdateTraineeDto;
import com.gymcrm.presentation.dto.request.UpdateTrainersDto;
import com.gymcrm.presentation.dto.response.TraineeWithTrainersAfterUpdateDto;
import com.gymcrm.presentation.dto.response.TraineeWithTrainersDto;
import com.gymcrm.presentation.dto.response.TrainerDto;
import com.gymcrm.presentation.dto.response.UserCredentialsDto;
import com.gymcrm.presentation.mapper.TraineeDtoMapper;
import com.gymcrm.presentation.mapper.TrainerDtoMapper;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Alish
 */
@RestController
public class TraineeController extends AbstractUserController implements TraineeControllerApi {
    private final TraineeService traineeService;

    @Autowired
    public TraineeController(TraineeService traineeService) {
        this.traineeService = traineeService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserCredentialsDto> registerTrainee(@RequestBody @Valid CreateTraineeDto request) {
        UserCredentials credentials = traineeService.createTrainee(TraineeDtoMapper.toDomain(request));
        return createUserCredentialsResponse(credentials, "trainees");
    }

    @GetMapping("/{username}")
    @PreAuthorize("#username == authentication.name")
    public TraineeWithTrainersDto getTraineeProfile(@PathVariable String username) {
        Trainee trainee = traineeService.getTraineeByUsername(username);
        return TraineeDtoMapper.toDtoWithTrainers(trainee);
    }

    @PutMapping("/{username}")
    @PreAuthorize("#username == authentication.name")
    public TraineeWithTrainersAfterUpdateDto updateTraineeProfile(
            @PathVariable String username,
            @RequestBody @Valid UpdateTraineeDto request
    ) {
        Trainee trainee = traineeService.updateTrainee(TraineeDtoMapper.toDomain(username, request));
        return TraineeDtoMapper.toDtoWithTrainersForUpdate(trainee);
    }

    @DeleteMapping("/{username}")
    @PreAuthorize("#username == authentication.name")
    public ResponseEntity<Void> deleteTraineeProfile(@PathVariable String username) {
        traineeService.deleteTrainee(username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{username}/trainers")
    @PreAuthorize("#username == authentication.name")
    public List<TrainerDto> getAvailableTrainers(@PathVariable String username) {
        List<Trainer> availableTrainers = traineeService.getAvailableTrainersForTrainee(username);
        return availableTrainers.stream().map(TrainerDtoMapper::toDto).toList();
    }

    @PutMapping("/{username}/trainers")
    @PreAuthorize("#username == authentication.name")
    public List<TrainerDto> updateTrainers(
            @PathVariable String username,
            @RequestBody @Valid UpdateTrainersDto updateTrainersDto
    ) {
        List<String> trainerUsernames = updateTrainersDto.getTrainerUsernames();
        List<Trainer> updatedTrainers = traineeService.updateTrainersForTrainee(username, trainerUsernames);
        return updatedTrainers.stream().map(TrainerDtoMapper::toDto).toList();
    }
}

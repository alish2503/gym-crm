package com.gymcrm.presentation.controller;

import com.gymcrm.application.response.UserCredentials;
import com.gymcrm.application.service.TraineeService;
import com.gymcrm.domain.model.Trainee;
import com.gymcrm.domain.model.Trainer;
import com.gymcrm.presentation.dto.request.CreateTraineeDto;
import com.gymcrm.presentation.dto.request.UpdateTraineeDto;
import com.gymcrm.presentation.dto.request.UpdateTrainersDto;
import com.gymcrm.presentation.dto.response.TraineeWithTrainersDto;
import com.gymcrm.presentation.dto.response.TrainerDto;
import com.gymcrm.presentation.dto.response.UserCredentialsDto;
import com.gymcrm.presentation.mapper.TraineeDtoMapper;
import com.gymcrm.presentation.mapper.TrainerDtoMapper;
import com.gymcrm.presentation.mapper.UserCredentialsDtoMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

/**
 * @author Alish
 */
@RestController
@RequestMapping("/trainees")
public class TraineeController extends UserController<TraineeService> {
    private final TraineeService traineeService;

    public TraineeController(TraineeService traineeService) {
        super(traineeService);
        this.traineeService = traineeService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserCredentialsDto> registerTrainee(@RequestBody @Valid CreateTraineeDto request) {
        UserCredentials credentials = traineeService.createTrainee(TraineeDtoMapper.toDomain(request));
        UserCredentialsDto credentialsDto = UserCredentialsDtoMapper.toDto(credentials);
        URI location = URI.create("/trainees/" + credentialsDto.username());
        return ResponseEntity.created(location).body(credentialsDto);
    }

    @GetMapping("/{username}")
    public TraineeWithTrainersDto getTraineeProfile(@PathVariable String username) {
        Trainee trainee = traineeService.getTraineeByUsername(username);
        return TraineeDtoMapper.toDtoWithTrainers(trainee);
    }

    @PutMapping("/{username}")
    public TraineeWithTrainersDto updateTraineeProfile(
            @PathVariable String username,
            @RequestBody @Valid UpdateTraineeDto request
    )
    {
        Trainee trainee = traineeService.updateTrainee(TraineeDtoMapper.toDomain(username, request));
        return TraineeDtoMapper.toDtoWithTrainers(trainee);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteTraineeProfile(@PathVariable String username) {
        traineeService.deleteTrainee(username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{username}/trainers")
    public List<TrainerDto> getAvailableTrainers(@PathVariable String username) {
        List<Trainer> availableTrainers = traineeService.getAvailableTrainersForTrainee(username);
        return availableTrainers.stream().map(TrainerDtoMapper::toDto).toList();
    }

    @PutMapping("/{username}/trainers")
    public List<TrainerDto> updateTrainers(@PathVariable String username,
                                           @RequestBody @Valid UpdateTrainersDto updateTrainersDto)
    {
        List<String> trainerUsernames = updateTrainersDto.getTrainerUsernames();
        List<Trainer> updatedTrainers = traineeService.updateTrainersForTrainee(username, trainerUsernames);
        return updatedTrainers.stream().map(TrainerDtoMapper::toDto).toList();
    }
}

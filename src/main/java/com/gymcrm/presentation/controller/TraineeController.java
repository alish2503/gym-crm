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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.validation.Valid;
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
@Api(value = "Trainee Management", tags = "Trainees")
public class TraineeController extends UserController<TraineeService> {
    private final TraineeService traineeService;

    public TraineeController(TraineeService traineeService) {
        super(traineeService);
        this.traineeService = traineeService;
    }

    @PostMapping("/register")
    @ApiOperation(value = "Register a new trainee", notes = "Creates a trainee account with generated username and password")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Trainee created successfully"),
            @ApiResponse(code = 400, message = "Invalid request data"),
    })
    public ResponseEntity<UserCredentialsDto> registerTrainee(@RequestBody @Valid CreateTraineeDto request) {
        UserCredentials credentials = traineeService.createTrainee(TraineeDtoMapper.toDomain(request));
        UserCredentialsDto credentialsDto = UserCredentialsDtoMapper.toDto(credentials);
        URI location = URI.create("/trainees/" + credentialsDto.username());
        return ResponseEntity.created(location).body(credentialsDto);
    }

    @GetMapping("/{username}")
    @ApiOperation(value = "Get trainee profile", notes = "Fetch trainee info including assigned trainers")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Trainee found"),
            @ApiResponse(code = 404, message = "Trainee not found")
    })
    public TraineeWithTrainersDto getTraineeProfile(@PathVariable String username) {
        Trainee trainee = traineeService.getTraineeByUsername(username);
        return TraineeDtoMapper.toDtoWithTrainers(trainee);
    }

    @PutMapping("/{username}")
    @ApiOperation(value = "Update trainee profile", notes = "Update trainee details like name, address, etc.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Trainee updated successfully"),
            @ApiResponse(code = 400, message = "Invalid request data"),
            @ApiResponse(code = 404, message = "Trainee not found")
    })
    public TraineeWithTrainersDto updateTraineeProfile(
            @PathVariable String username,
            @RequestBody @Valid UpdateTraineeDto request
    ) {
        Trainee trainee = traineeService.updateTrainee(TraineeDtoMapper.toDomain(username, request));
        return TraineeDtoMapper.toDtoWithTrainers(trainee);
    }

    @DeleteMapping("/{username}")
    @ApiOperation(value = "Delete trainee profile", notes = "Deletes trainee and associated trainings")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Trainee deleted successfully"),
            @ApiResponse(code = 404, message = "Trainee not found")
    })
    public ResponseEntity<Void> deleteTraineeProfile(@PathVariable String username) {
        traineeService.deleteTrainee(username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{username}/trainers")
    @ApiOperation(value = "Get available trainers for trainee", notes = "Fetch trainers that can be assigned to the trainee")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of available trainers"),
            @ApiResponse(code = 404, message = "Trainee not found")
    })
    public List<TrainerDto> getAvailableTrainers(@PathVariable String username) {
        List<Trainer> availableTrainers = traineeService.getAvailableTrainersForTrainee(username);
        return availableTrainers.stream().map(TrainerDtoMapper::toDto).toList();
    }

    @PutMapping("/{username}/trainers")
    @ApiOperation(value = "Update assigned trainers for trainee", notes = "Assign a list of trainers to the trainee")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Trainers updated successfully"),
            @ApiResponse(code = 404, message = "Trainee or trainer not found")
    })
    public List<TrainerDto> updateTrainers(
            @PathVariable String username,
            @RequestBody @Valid UpdateTrainersDto updateTrainersDto
    ) {
        List<String> trainerUsernames = updateTrainersDto.getTrainerUsernames();
        List<Trainer> updatedTrainers = traineeService.updateTrainersForTrainee(username, trainerUsernames);
        return updatedTrainers.stream().map(TrainerDtoMapper::toDto).toList();
    }
}

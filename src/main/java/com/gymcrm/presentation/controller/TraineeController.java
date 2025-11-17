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


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
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
@RequestMapping(path = "/trainees", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Trainees", description = "Endpoints for managing trainees")
public class TraineeController extends UserController<TraineeService> {

    private final TraineeService traineeService;

    public TraineeController(TraineeService traineeService) {
        super(traineeService);
        this.traineeService = traineeService;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new trainee", description = "Creates a trainee account with generated username and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Trainee created successfully",
                    content = @Content(schema = @Schema(implementation = UserCredentialsDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content)
    })
    public ResponseEntity<UserCredentialsDto> registerTrainee(@RequestBody @Valid CreateTraineeDto request) {
        UserCredentials credentials = traineeService.createTrainee(TraineeDtoMapper.toDomain(request));
        UserCredentialsDto credentialsDto = UserCredentialsDtoMapper.toDto(credentials);
        URI location = URI.create("/trainees/" + credentialsDto.username());
        return ResponseEntity.created(location).body(credentialsDto);
    }

    @GetMapping("/{username}")
    @Operation(summary = "Get trainee profile", description = "Fetch trainee info including assigned trainers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainee found",
                    content = @Content(schema = @Schema(implementation = TraineeWithTrainersDto.class))),
            @ApiResponse(responseCode = "404", description = "Trainee not found", content = @Content)
    })
    public TraineeWithTrainersDto getTraineeProfile(@PathVariable String username) {
        Trainee trainee = traineeService.getTraineeByUsername(username);
        return TraineeDtoMapper.toDtoWithTrainers(trainee);
    }

    @PutMapping("/{username}")
    @Operation(summary = "Update trainee profile", description = "Update trainee details like name, address, etc.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainee updated successfully",
                    content = @Content(schema = @Schema(implementation = TraineeWithTrainersDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Trainee not found", content = @Content)
    })
    public TraineeWithTrainersDto updateTraineeProfile(
            @PathVariable String username,
            @RequestBody @Valid UpdateTraineeDto request
    ) {
        Trainee trainee = traineeService.updateTrainee(TraineeDtoMapper.toDomain(username, request));
        return TraineeDtoMapper.toDtoWithTrainers(trainee);
    }

    @DeleteMapping("/{username}")
    @Operation(summary = "Delete trainee profile", description = "Deletes trainee and associated trainings")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Trainee deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Trainee not found")
    })
    public ResponseEntity<Void> deleteTraineeProfile(@PathVariable String username) {
        traineeService.deleteTrainee(username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{username}/trainers")
    @Operation(summary = "Get available trainers for trainee", description = "Fetch trainers that can be assigned to the trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of available trainers",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = TrainerDto.class)))),

            @ApiResponse(responseCode = "404", description = "Trainee not found", content = @Content)
    })
    public List<TrainerDto> getAvailableTrainers(@PathVariable String username) {
        List<Trainer> availableTrainers = traineeService.getAvailableTrainersForTrainee(username);
        return availableTrainers.stream().map(TrainerDtoMapper::toDto).toList();
    }

    @PutMapping("/{username}/trainers")
    @Operation(summary = "Update assigned trainers for trainee", description = "Assign a list of trainers to the trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainers updated successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = TrainerDto.class)))),
            @ApiResponse(responseCode = "404", description = "Trainee or trainer not found", content = @Content)
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

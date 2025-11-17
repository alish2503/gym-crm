package com.gymcrm.presentation.controller;

import com.gymcrm.application.response.UserCredentials;
import com.gymcrm.application.service.TrainerService;
import com.gymcrm.domain.model.Trainer;
import com.gymcrm.presentation.dto.request.CreateTrainerDto;
import com.gymcrm.presentation.dto.request.UpdateUserDto;
import com.gymcrm.presentation.dto.response.TrainerWithTraineesDto;
import com.gymcrm.presentation.dto.response.UserCredentialsDto;
import com.gymcrm.presentation.mapper.TrainerDtoMapper;
import com.gymcrm.presentation.mapper.UserCredentialsDtoMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

/**
 * @author Alish
 */
@RestController
@RequestMapping(path = "/trainers", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Trainers", description = "Endpoints for managing trainers")
public class TrainerController extends UserController<TrainerService> {

    private final TrainerService trainerService;

    public TrainerController(TrainerService trainerService) {
        super(trainerService);
        this.trainerService = trainerService;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new trainer", description = "Creates a trainer account with generated username and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Trainer created successfully",
                    content = @Content(schema = @Schema(implementation = UserCredentialsDto.class))),

            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content)
    })
    public ResponseEntity<UserCredentialsDto> registerTrainer(@RequestBody @Valid CreateTrainerDto request) {
        UserCredentials credentials = trainerService.createTrainer(TrainerDtoMapper.toDomain(request));
        UserCredentialsDto credentialsDto = UserCredentialsDtoMapper.toDto(credentials);
        URI location = URI.create("/trainers/" + credentialsDto.username());
        return ResponseEntity.created(location).body(credentialsDto);
    }

    @GetMapping("/{username}")
    @Operation(summary = "Get trainer profile", description = "Fetch trainer information including assigned trainees")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainer found",
                    content = @Content(schema = @Schema(implementation = TrainerWithTraineesDto.class))),

            @ApiResponse(responseCode = "404", description = "Trainer not found", content = @Content)
    })
    public TrainerWithTraineesDto getTrainerProfile(@PathVariable String username) {
        Trainer trainer = trainerService.getTrainerByUsername(username);
        return TrainerDtoMapper.toDtoWithTrainees(trainer);
    }

    @PutMapping("/{username}")
    @Operation(summary = "Update trainer profile", description = "Update trainer details like name, specialization, etc.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainer updated successfully",
                    content = @Content(schema = @Schema(implementation = TrainerWithTraineesDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Trainer not found", content = @Content)
    })
    public TrainerWithTraineesDto updateTrainerProfile(
            @PathVariable String username,
            @RequestBody @Valid UpdateUserDto request
    ) {
        Trainer trainer = trainerService.updateTrainer(TrainerDtoMapper.toDomain(username, request));
        return TrainerDtoMapper.toDtoWithTrainees(trainer);
    }
}
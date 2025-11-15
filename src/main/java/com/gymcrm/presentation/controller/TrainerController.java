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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.validation.Valid;
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
@RequestMapping("/trainers")
@Api(value = "Trainer Management", tags = "Trainers")
public class TrainerController extends UserController<TrainerService> {

    private final TrainerService trainerService;

    public TrainerController(TrainerService trainerService) {
        super(trainerService);
        this.trainerService = trainerService;
    }

    @PostMapping("/register")
    @ApiOperation(value = "Register a new trainer", notes = "Creates a trainer account with generated username and password")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Trainer created successfully"),
            @ApiResponse(code = 400, message = "Invalid request data")
    })
    public ResponseEntity<UserCredentialsDto> registerTrainer(@RequestBody @Valid CreateTrainerDto request) {
        UserCredentials credentials = trainerService.createTrainer(TrainerDtoMapper.toDomain(request));
        UserCredentialsDto credentialsDto = UserCredentialsDtoMapper.toDto(credentials);
        URI location = URI.create("/trainers/" + credentialsDto.username());
        return ResponseEntity.created(location).body(credentialsDto);
    }

    @GetMapping("/{username}")
    @ApiOperation(value = "Get trainer profile", notes = "Fetch trainer information including assigned trainees")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Trainer found"),
            @ApiResponse(code = 404, message = "Trainer not found")
    })
    public TrainerWithTraineesDto getTrainerProfile(@PathVariable String username) {
        Trainer trainer = trainerService.getTrainerByUsername(username);
        return TrainerDtoMapper.toDtoWithTrainees(trainer);
    }

    @PutMapping("/{username}")
    @ApiOperation(value = "Update trainer profile", notes = "Update trainer details like name, specialization, etc.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Trainer updated successfully"),
            @ApiResponse(code = 400, message = "Invalid request data"),
            @ApiResponse(code = 404, message = "Trainer not found")
    })
    public TrainerWithTraineesDto updateTrainerProfile(
            @PathVariable String username,
            @RequestBody @Valid UpdateUserDto request
    ) {
        Trainer trainer = trainerService.updateTrainer(TrainerDtoMapper.toDomain(username, request));
        return TrainerDtoMapper.toDtoWithTrainees(trainer);
    }
}

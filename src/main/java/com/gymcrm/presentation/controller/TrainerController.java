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
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
public class TrainerController extends UserController<TrainerService> {
    private final TrainerService trainerService;

    public TrainerController(TrainerService trainerService) {
        super(trainerService);
        this.trainerService = trainerService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserCredentialsDto> registerTrainer(@RequestBody @Valid CreateTrainerDto request) {
        UserCredentials credentials = trainerService.createTrainer(TrainerDtoMapper.toDomain(request));
        UserCredentialsDto credentialsDto = UserCredentialsDtoMapper.toDto(credentials);
        URI location = URI.create("/trainers/" + credentialsDto.username());
        return ResponseEntity.created(location).body(credentialsDto);
    }

    @GetMapping("/{username}")
    public TrainerWithTraineesDto getTrainerProfile(@PathVariable String username) {
        Trainer trainer = trainerService.getTrainerByUsername(username);
        return TrainerDtoMapper.toDtoWithTrainees(trainer);
    }

    @PutMapping("/{username}")
    public TrainerWithTraineesDto updateTrainerProfile(
            @PathVariable String username,
            @RequestBody @Valid UpdateUserDto request
    )
    {
        Trainer trainer = trainerService.updateTrainer(TrainerDtoMapper.toDomain(username, request));
        return TrainerDtoMapper.toDtoWithTrainees(trainer);
    }
}

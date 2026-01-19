package com.gymcrm.presentation.controller;

import com.gymcrm.presentation.dto.request.CreateTrainerDto;
import com.gymcrm.presentation.dto.request.UpdateTrainerDto;
import com.gymcrm.presentation.dto.response.TrainerWithTraineesAfterUpdateDto;
import com.gymcrm.presentation.dto.response.TrainerWithTraineesDto;
import com.gymcrm.presentation.dto.response.UserCredentialsDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Alish
 */
@Tag(name = "Trainers")
@RequestMapping(path = "/trainers", produces = "application/json")
public interface TrainerControllerApi {

    @Operation(summary = "Register a new trainer",
            description = "Creates a trainer account with generated username and password"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Trainer created successfully",
                    content = @Content(schema = @Schema(implementation = UserCredentialsDto.class))),

            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Invalid token", content = @Content)
    })
    ResponseEntity<UserCredentialsDto> registerTrainer(CreateTrainerDto request);

    @Operation(summary = "Get trainer profile", description = "Fetch trainer information including assigned trainees")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainer found",
                    content = @Content(schema = @Schema(implementation = TrainerWithTraineesDto.class))),
            @ApiResponse(responseCode = "401", description = "Invalid token", content = @Content),
            @ApiResponse(responseCode = "404", description = "Trainer not found", content = @Content)
    })
    TrainerWithTraineesDto getTrainerProfile(String username);

    @Operation(summary = "Update trainer profile", description = "Update trainer details like name, specialization, etc.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainer updated successfully",
                    content = @Content(schema = @Schema(implementation = TrainerWithTraineesAfterUpdateDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Invalid token", content = @Content),
            @ApiResponse(responseCode = "404", description = "Trainer not found", content = @Content)
    })
    TrainerWithTraineesAfterUpdateDto updateTrainerProfile(String username, UpdateTrainerDto request);
}

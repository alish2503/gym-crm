package com.gymcrm.presentation.controller.port;

import com.gymcrm.presentation.dto.request.ChangePasswordDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Alish
 */
@Tag(name = "Users")
@RequestMapping(path = "/users", produces = "application/json")
public interface UserControllerApi {

    @Operation(summary = "Change user active status", description = "Enable or disable a user account by username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User activity updated successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid token"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    void changeActivity(String username);

    @Operation(summary = "Change user password", description = "Set a new password for the user by username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Wrong password or invalid token",
                    content = @Content)
    })
    ResponseEntity<?> setNewPassword(String username, ChangePasswordDto dto);
}

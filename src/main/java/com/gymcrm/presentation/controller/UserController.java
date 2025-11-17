package com.gymcrm.presentation.controller;

import com.gymcrm.application.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Alish
 */
abstract class UserController<T extends UserService> {
    private final T userService;

    public UserController(T userService) {
        this.userService = userService;
    }

    @PatchMapping("/{username}/active")
    @Operation(summary = "Set user active status", description = "Enable or disable a user account by username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User activity updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Void> setActivity(@PathVariable String username, @RequestParam boolean isActive)
    {
        userService.toggle(username, isActive);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{username}/password")
    @Operation(summary = "Change user password", description = "Set a new password for the user by username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Void> setNewPassword(@PathVariable String username, @RequestParam String password)
    {
        userService.changePassword(username, password);
        return ResponseEntity.ok().build();
    }
}

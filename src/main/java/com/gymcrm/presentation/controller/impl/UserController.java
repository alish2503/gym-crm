package com.gymcrm.presentation.controller.impl;

import com.gymcrm.application.service.UserService;
import com.gymcrm.infrastructure.security.service.CustomUserDetailsService;
import com.gymcrm.presentation.controller.UserControllerApi;
import com.gymcrm.presentation.dto.request.ChangePasswordDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Alish
 */
@RestController
public class UserController implements UserControllerApi {
    private final UserService userService;
    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public UserController(UserService userService, CustomUserDetailsService customUserDetailsService) {
        this.userService = userService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @PatchMapping("/{username}/active")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("#username == authentication.name")
    public void changeActivity(@PathVariable String username) {
        userService.toggle(username);
    }

    @PatchMapping("/{username}/password")
    @PreAuthorize("#username == authentication.name")
    public ResponseEntity<?> setNewPassword(@PathVariable String username,
                                            @RequestBody @Valid ChangePasswordDto dto)
    {
        if (!customUserDetailsService.isValidPassword(username, dto.getOldPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Wrong password"));
        }
        userService.changePassword(username, dto.getNewPassword());
        return ResponseEntity.ok().build();
    }
}

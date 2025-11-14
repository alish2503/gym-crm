package com.gymcrm.presentation.controller;

import com.gymcrm.application.service.UserService;
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
    public ResponseEntity<Void> setActivity(@PathVariable String username, @RequestParam boolean isActive)
    {
        userService.toggle(username, isActive);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{username}/password")
    public ResponseEntity<Void> setNewPassword(@PathVariable String username, @RequestParam String password)
    {
        userService.changePassword(username, password);
        return ResponseEntity.ok().build();
    }
}

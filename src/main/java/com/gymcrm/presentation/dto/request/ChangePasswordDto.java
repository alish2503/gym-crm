package com.gymcrm.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * @author Alish
 */
public class ChangePasswordDto {

    @Schema(example = "password")
    @NotBlank(message = "Current password required")
    private String oldPassword;

    @Schema(example = "newPassword")
    @NotBlank(message = "New password required")
    private String newPassword;

    public ChangePasswordDto() {}

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}

package com.gymcrm.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Alish
 */
@Setter
@Getter
@NoArgsConstructor
public class ChangePasswordDto {

    @Schema(example = "password")
    @NotBlank(message = "Current password required")
    private String oldPassword;

    @Schema(example = "newPassword")
    @NotBlank(message = "New password required")
    private String newPassword;

    public ChangePasswordDto(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }
}

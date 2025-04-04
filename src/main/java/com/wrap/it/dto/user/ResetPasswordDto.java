package com.wrap.it.dto.user;

import jakarta.validation.constraints.NotBlank;

public record ResetPasswordDto(@NotBlank String newPassword,
                               @NotBlank String code,
                               @NotBlank String email){

}

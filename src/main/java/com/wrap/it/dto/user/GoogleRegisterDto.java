package com.wrap.it.dto.user;

import com.wrap.it.annotation.PasswordMatches;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@PasswordMatches
public record GoogleRegisterDto(@NotBlank @Pattern(regexp = "^\\+?[0-9]{7,15}$",
        message = "Invalid phone format") String phoneNumber, @NotBlank String password,
                                @NotBlank String repeatPassword) {
}

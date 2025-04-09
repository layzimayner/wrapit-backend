package com.wrap.it.dto.user;

import com.wrap.it.annotation.PasswordMatches;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@PasswordMatches
public class ResetPasswordDto extends PasswordAwareRequest {
    @NotBlank
    private String code;

    @Email
    private String email;

}

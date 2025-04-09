package com.wrap.it.dto.user;

import com.wrap.it.annotation.PasswordMatches;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@PasswordMatches
public class UserRegistrationRequestDto extends PasswordAwareRequest {
    @Email
    private String email;

    @NotBlank
    private String code;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private String middleName;

    private String shippingAddress;

}

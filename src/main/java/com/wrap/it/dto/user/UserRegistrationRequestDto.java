package com.wrap.it.dto.user;

import com.wrap.it.annotation.PasswordMatches;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@PasswordMatches
public class UserRegistrationRequestDto {
    @NotBlank
    @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Invalid phone format")
    private String phoneNumber;
    @NotBlank
    private String password;
    @NotBlank
    private String repeatPassword;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;

    private String middleName;

    private String shippingAddress;

}

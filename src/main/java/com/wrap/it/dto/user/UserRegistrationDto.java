package com.wrap.it.dto.user;

import lombok.Data;

@Data
public class UserRegistrationDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
}

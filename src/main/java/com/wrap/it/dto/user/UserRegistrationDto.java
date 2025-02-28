package com.wrap.it.dto.user;

import lombok.Data;

@Data
public class UserRegistrationDto {
    private Long id;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String middleName;
    private String shippingAddress;
}

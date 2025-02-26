package com.wrap.it.dto.user;

import lombok.Data;

import java.util.List;

@Data
public class UserWithRoleDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private List<Long> rolesIds;
}

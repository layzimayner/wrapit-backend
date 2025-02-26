package com.wrap.it.dto.user;

import java.util.List;
import lombok.Data;

@Data
public class UserWithRoleDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private List<Long> rolesIds;
}

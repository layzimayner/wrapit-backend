package com.wrap.it.mapper;

import com.wrap.it.config.MapperConfig;
import com.wrap.it.dto.user.UpdateUserInfoDto;
import com.wrap.it.dto.user.UserRegistrationDto;
import com.wrap.it.dto.user.UserRegistrationRequestDto;
import com.wrap.it.dto.user.UserWithRoleDto;
import com.wrap.it.model.Role;
import com.wrap.it.model.User;
import java.util.List;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, componentModel = "spring")
public interface UserMapper {
    User toModel(UserRegistrationRequestDto requestDto);

    UserRegistrationDto toDto(User user);

    UserWithRoleDto toModelWithRoles(User user);

    void update(@MappingTarget User user, UpdateUserInfoDto requestDto);

    @AfterMapping
    default void setRolesIds(@MappingTarget UserWithRoleDto userDto, User user) {
        List<Long> rolesIds = user.getRoles().stream()
                .map(Role::getId)
                .toList();
        userDto.setRolesIds(rolesIds);
    }
}

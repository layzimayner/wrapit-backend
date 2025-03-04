package com.wrap.it.service.impl;

import com.wrap.it.dto.role.RoleRequestDto;
import com.wrap.it.dto.user.UserRegistrationDto;
import com.wrap.it.dto.user.UserRegistrationRequestDto;
import com.wrap.it.dto.user.UserWithRoleDto;
import com.wrap.it.exception.EntityNotFoundException;
import com.wrap.it.exception.RegistrationException;
import com.wrap.it.mapper.UserMapper;
import com.wrap.it.model.Role;
import com.wrap.it.model.User;
import com.wrap.it.repository.RoleRepository;
import com.wrap.it.repository.UserRepository;
import com.wrap.it.service.CartService;
import com.wrap.it.service.UserService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final CartService cartService;

    @Override
    public UserRegistrationDto save(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.existsByPhoneNumber(requestDto.getPhoneNumber())) {
            throw new RegistrationException("Phone number is already in use");
        }
        User user = userMapper.toModel(requestDto);
        user.setRoles(Set.of(roleRepository.findByName(Role.RoleName.USER)));
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        userRepository.save(user);
        cartService.createShoppingCartForUser(user);
        return userMapper.toDto(user);
    }

    @Override
    public UserWithRoleDto updateRoles(RoleRequestDto requestDto, Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't update user with id "
                        + id + " because it does not exist")
        );
        user.setRoles(roleRepository.findRolesByIds(requestDto.rolesIds()));
        userRepository.save(user);
        return userMapper.toModelWithRoles(user);
    }

    @Override
    public UserRegistrationDto findInfo(User user) {
        return userMapper.toDto(user);
    }

    @Override
    public UserRegistrationDto updateProfile(User user, UserRegistrationRequestDto requestDto) {
        requestDto.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        userMapper.update(user, requestDto);
        userRepository.save(user);
        return userMapper.toDto(user);
    }
}

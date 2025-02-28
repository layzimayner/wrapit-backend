package com.wrap.it.service.impl;

import com.wrap.it.dto.user.UserRegistrationDto;
import com.wrap.it.dto.user.UserRegistrationRequestDto;
import com.wrap.it.exception.RegistrationException;
import com.wrap.it.mapper.UserMapper;
import com.wrap.it.model.Role;
import com.wrap.it.model.User;
import com.wrap.it.repository.RoleRepository;
import com.wrap.it.repository.UserRepository;
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
        return userMapper.toDto(user);
    }
}

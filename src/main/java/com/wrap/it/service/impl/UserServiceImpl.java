package com.wrap.it.service.impl;

import com.wrap.it.dto.role.RoleRequestDto;
import com.wrap.it.dto.user.GoogleRegisterDto;
import com.wrap.it.dto.user.UserLoginResponseDto;
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
import com.wrap.it.security.AuthenticationService;
import com.wrap.it.service.CartService;
import com.wrap.it.service.UserService;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final int FIRST_NAME_POSITION = 0;
    private static final int LAST_NAME_POSITION = 0;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final CartService cartService;
    private final AuthenticationService authenticationService;

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

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void updatePassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new EntityNotFoundException("Can't update user with email "
                        + email + " because it does not exist")
        );
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public void updateEmail(String email, User user) {
        user.setEmail(email);
        userRepository.save(user);
    }

    @Override
    public UserLoginResponseDto googleLogin(Map<String, Object> attributes,
                                            GoogleRegisterDto request) {
        String email = String.valueOf(attributes.get("email"));
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return authenticationService.authenticate(user.get());
        }

        String fullNameStr = String.valueOf(attributes.get("name"));
        String[] fullName = fullNameStr.split(" ");

        String firstName = fullName.length > 0 ? fullName[FIRST_NAME_POSITION] : "Unknown";
        String lastName = fullName.length > 1 ? fullName[LAST_NAME_POSITION] : "User";

        User newUser = new User();
        newUser.setEmail(email);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setPhoneNumber(request.phoneNumber());
        newUser.setPassword(passwordEncoder.encode(request.password()));
        newUser.setRoles(Set.of(roleRepository.findByName(Role.RoleName.USER)));

        userRepository.save(newUser);

        cartService.createShoppingCartForUser(newUser);

        return authenticationService.authenticate(newUser);
    }
}

package com.wrap.it.service;

import com.wrap.it.dto.user.UserRegistrationDto;
import com.wrap.it.dto.user.UserRegistrationRequestDto;
import com.wrap.it.exception.RegistrationException;

public interface UserService {
    UserRegistrationDto save(UserRegistrationRequestDto requestDto)
            throws RegistrationException;
}

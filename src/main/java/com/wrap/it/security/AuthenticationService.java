package com.wrap.it.security;

import com.wrap.it.dto.user.UserLoginRequestDto;
import com.wrap.it.dto.user.UserLoginResponseDto;
import com.wrap.it.model.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public UserLoginResponseDto authenticate(UserLoginRequestDto requestDto) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDto.phoneNumber(),
                        requestDto.password())
        );

        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        String token = jwtUtil.generateToken(authentication.getName(), roles);

        User user = (User)authentication.getPrincipal();

        return new UserLoginResponseDto(token,
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber());
    }
}

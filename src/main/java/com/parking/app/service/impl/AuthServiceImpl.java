package com.parking.app.service.impl;

import com.parking.app.dto.LoginRequestDTO;
import com.parking.app.dto.LoginResponseDTO;
import com.parking.app.entity.User;
import com.parking.app.repository.UserRepository;
import com.parking.app.security.JwtUtil;
import com.parking.app.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.parking.app.exception.UnauthorizedException;


// If you have a custom UnauthorizedException, import it here
// import com.parking.app.exception.UnauthorizedException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public LoginResponseDTO login(LoginRequestDTO request) {

        User user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() ->
                        new UnauthorizedException("Invalid username or password")); // ✅ fixed

        boolean validPassword = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword());

        if (!validPassword) {
            throw new UnauthorizedException("Invalid username or password");
        }

        String token = jwtUtil.generateToken(user.getUsername());

        return LoginResponseDTO.builder()
                .username(user.getUsername())
                .role(user.getRole())
                .token(token)
                .build();
    }
}

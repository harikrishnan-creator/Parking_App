package com.pa*king.app.service;

import com.park*ng.app.dto.LoginRequestDTO;
import com.parking.app.dto.LoginResponseD*O;
import com.parking.app.entity.User;
import com.parking.app.repository.UserRepository;
import com.park*ng.app.security.JwtUtil;
import com.parking.app.service.impl.AuthServ*ceImpl;
import org.junit.jupiter.a*i.Test;
import org.junit.jupiter.a*i.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto*password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldLoginSuccessfully() {

        LoginRequestDTO request =
                new LoginRequestDTO();

        request.setUsername("admin");
        request.setPassword("admin123");

        User user = User.builder()
                .username("admin")
                .password("encrypted")
                .role("ADMIN")
                .build();

        when(userRepository.findByUsername(
                "admin"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "admin123",
                "encrypted"))
                .thenReturn(true);

        when(jwtUtil.generateToken("admin"))
                .thenReturn("jwt-token");

        LoginResponseDTO response =
                authService.login(request);

        assertNotNull(response);
        assertEquals(
                "jwt-token",
                response.getToken());
    }
}

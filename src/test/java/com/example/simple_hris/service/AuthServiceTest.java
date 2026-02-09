package com.example.simple_hris.service;

import com.example.simple_hris.dto.request.AddSuperadminRequestDTO;
import com.example.simple_hris.dto.request.UserLoginRequestDTO;
import com.example.simple_hris.entity.User;
import com.example.simple_hris.enums.Role;
import com.example.simple_hris.exception.custom.BusinessException;
import com.example.simple_hris.repository.UserRepository;
import com.example.simple_hris.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    private AddSuperadminRequestDTO registerRequest;
    private UserLoginRequestDTO loginRequest;
    private User user;

    @BeforeEach
    void setUp() {
        registerRequest = new AddSuperadminRequestDTO();
        registerRequest.setUsername("admin");
        registerRequest.setPassword("password123");

        loginRequest = new UserLoginRequestDTO();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("password123");

        user = new User();
        user.setUsername("admin");
        user.setPassword("encodedPassword");
        user.setRole(Role.SUPERADMIN);
    }

    @Test
    void register_shouldSaveUser_whenRequestIsValid() {
        when(encoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");

        authService.register(registerRequest);

        verify(encoder, times(1)).encode(registerRequest.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void login_shouldReturnToken_whenCredentialsAreValid() {
        when(userRepository.findByUsername(loginRequest.getUsername())).thenReturn(Optional.of(user));
        when(encoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtUtil.generateToken(loginRequest.getUsername())).thenReturn("mocked-jwt-token");

        String token = authService.login(loginRequest);

        assertNotNull(token);
        assertEquals("mocked-jwt-token", token);
        verify(userRepository, times(1)).findByUsername(loginRequest.getUsername());
        verify(encoder, times(1)).matches(loginRequest.getPassword(), user.getPassword());
        verify(jwtUtil, times(1)).generateToken(loginRequest.getUsername());
    }

    @Test
    void login_shouldThrowException_whenUserNotFound() {
        when(userRepository.findByUsername(loginRequest.getUsername())).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            authService.login(loginRequest);
        });

        assertEquals("Username atau password salah!", exception.getMessage());
        verify(userRepository, times(1)).findByUsername(loginRequest.getUsername());
        verify(encoder, never()).matches(anyString(), anyString());
        verify(jwtUtil, never()).generateToken(anyString());
    }

    @Test
    void login_shouldThrowException_whenPasswordIsWrong() {
        when(userRepository.findByUsername(loginRequest.getUsername())).thenReturn(Optional.of(user));
        when(encoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(false);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            authService.login(loginRequest);
        });

        assertEquals("Username atau password salah!", exception.getMessage());
        verify(userRepository, times(1)).findByUsername(loginRequest.getUsername());
        verify(encoder, times(1)).matches(loginRequest.getPassword(), user.getPassword());
        verify(jwtUtil, never()).generateToken(anyString());
    }
}

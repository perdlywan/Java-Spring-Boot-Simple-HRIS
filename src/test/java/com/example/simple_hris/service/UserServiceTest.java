package com.example.simple_hris.service;

import com.example.simple_hris.dto.request.UpdateUserRequestDTO;
import com.example.simple_hris.dto.request.UpdateUserRoleRequestDTO;
import com.example.simple_hris.entity.User;
import com.example.simple_hris.enums.Role;
import com.example.simple_hris.exception.custom.DataNotFoundException;
import com.example.simple_hris.repository.UserRepository;
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
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private UserService userService;

    private User user;
    private UpdateUserRequestDTO updateRequest;
    private UpdateUserRoleRequestDTO updateRoleRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("oldUser");
        user.setPassword("oldPassword");
        user.setRole(Role.EMPLOYEE);

        updateRequest = new UpdateUserRequestDTO();
        updateRequest.setUsername("newUser");
        updateRequest.setPassword("newPassword");

        updateRoleRequest = new UpdateUserRoleRequestDTO();
        updateRoleRequest.setRole(Role.HRADMIN);
    }

    @Test
    void updateUser_shouldUpdateUser_whenUserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(encoder.encode("newPassword")).thenReturn("encodedNewPassword");

        userService.updateUser(1L, updateRequest);

        assertEquals("newUser", user.getUsername());
        assertEquals("encodedNewPassword", user.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateUser_shouldThrowDataNotFoundException_whenUserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> userService.updateUser(1L, updateRequest));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateUserRole_shouldUpdateRole_whenUserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.updateUserRole(1L, updateRoleRequest);

        assertEquals(Role.HRADMIN, user.getRole());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateUserRole_shouldThrowDataNotFoundException_whenUserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> userService.updateUserRole(1L, updateRoleRequest));
        verify(userRepository, never()).save(any(User.class));
    }
}

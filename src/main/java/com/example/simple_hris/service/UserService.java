package com.example.simple_hris.service;

import com.example.simple_hris.dto.request.UpdateUserRequestDTO;
import com.example.simple_hris.dto.request.UpdateUserRoleRequestDTO;
import com.example.simple_hris.entity.User;
import com.example.simple_hris.exception.custom.DataNotFoundException;
import com.example.simple_hris.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Transactional
    @PreAuthorize("hasAnyRole('SUPERADMIN','HRADMIN')")
    public void updateUser(Long id, UpdateUserRequestDTO request){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("User tidak terdaftar"));

        updateIfPresent(request.getUsername(), user::setUsername);
        updateIfPresent(request.getPassword(),
                p -> user.setPassword(encoder.encode(p)));
        userRepository.save(user);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SUPERADMIN','HRADMIN')")
    public void updateUserRole(Long id, UpdateUserRoleRequestDTO request){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Employee tidak terdaftar"));

        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }

        userRepository.save(user);
    }

    private void updateIfPresent(String value, Consumer<String> setter) {
        if (value != null && !value.isBlank()) {
            setter.accept(value);
        }
    }
}

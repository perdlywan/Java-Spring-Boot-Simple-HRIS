package com.example.simple_hris.service;

import com.example.simple_hris.dto.request.AddSuperadminRequestDTO;
import com.example.simple_hris.entity.User;
import com.example.simple_hris.enums.Role;
import com.example.simple_hris.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public void register(AddSuperadminRequestDTO request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRole(Role.SUPERADMIN);
        userRepository.save(user);
    }
}

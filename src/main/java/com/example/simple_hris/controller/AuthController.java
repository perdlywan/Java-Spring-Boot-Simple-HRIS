package com.example.simple_hris.controller;

import com.example.simple_hris.dto.request.AddSuperadminRequestDTO;
import com.example.simple_hris.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public void register(@RequestBody AddSuperadminRequestDTO request) {
        authService.register(request);
    }
}

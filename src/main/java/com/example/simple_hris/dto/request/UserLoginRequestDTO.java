package com.example.simple_hris.dto.request;

import lombok.Data;

@Data
public class UserLoginRequestDTO {
    private String username;
    private String password;
}

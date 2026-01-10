package com.example.simple_hris.dto.request;

import com.example.simple_hris.enums.Role;
import lombok.Data;

@Data
public class UpdateUserRequestDTO {
    private String username;
    private String password;
}

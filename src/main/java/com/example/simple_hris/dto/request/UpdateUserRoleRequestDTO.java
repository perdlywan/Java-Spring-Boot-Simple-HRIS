package com.example.simple_hris.dto.request;

import com.example.simple_hris.enums.Role;
import lombok.Data;

@Data
public class UpdateUserRoleRequestDTO {
    private Role role;
}

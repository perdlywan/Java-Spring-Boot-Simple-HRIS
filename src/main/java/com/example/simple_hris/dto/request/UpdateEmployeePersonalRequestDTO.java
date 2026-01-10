package com.example.simple_hris.dto.request;

import com.example.simple_hris.enums.EmploymentStatus;
import com.example.simple_hris.enums.MaritalStatus;
import com.example.simple_hris.enums.Religion;
import com.example.simple_hris.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class UpdateEmployeePersonalRequestDTO {
    private String firstName;
    private String middleName;
    private String lastName;
    private String placeOfBirth;
    private LocalDate dateOfBirth;
    private Religion religion;
    private MaritalStatus maritalStatus;
}

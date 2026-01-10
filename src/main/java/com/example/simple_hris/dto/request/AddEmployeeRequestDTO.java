package com.example.simple_hris.dto.request;

import com.example.simple_hris.entity.Position;
import com.example.simple_hris.enums.EmploymentStatus;
import com.example.simple_hris.enums.MaritalStatus;
import com.example.simple_hris.enums.Religion;
import com.example.simple_hris.enums.Role;
import lombok.Data;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AddEmployeeRequestDTO {
    @NotBlank(message = "Username wajib diisi")
    private String username;

    @NotBlank(message = "Password wajib diisi")
    private String password;

    @NotNull(message = "Role wajib diisi")
    private Role role;

    @NotBlank(message = "first Name wajib diisi")
    private String firstName;

    private String middleName;
    private String lastName;

    @NotNull(message = "Hire date wajib diisi")
    private LocalDate hireDate;

    @NotBlank(message = "placeOfBirth wajib diisi")
    private String placeOfBirth;

    @NotNull(message = "dateOfBirth wajib diisi")
    private LocalDate dateOfBirth;

    @NotNull(message = "religion wajib diisi")
    private Religion religion;

    @NotNull(message = "maritalStatus wajib diisi")
    private MaritalStatus maritalStatus;

    @NotNull(message = "positionId wajib diisi")
    private Long positionId;

    private LocalDate endDate;

    @NotNull(message = "employmentStatus wajib diisi")
    private EmploymentStatus employmentStatus;
    private String jobDescription;
    private BigDecimal salary;
}

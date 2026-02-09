package com.example.simple_hris.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateEmployeeSalaryRequestDTO {
    @NotNull(message="Salary tidak boleh kosong")
    private BigDecimal salary;
}

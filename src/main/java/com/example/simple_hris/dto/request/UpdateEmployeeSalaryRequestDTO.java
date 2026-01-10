package com.example.simple_hris.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateEmployeeSalaryRequestDTO {
    @NotNull(message="Salary tidak boleh kosong")
    private BigDecimal salary;
}

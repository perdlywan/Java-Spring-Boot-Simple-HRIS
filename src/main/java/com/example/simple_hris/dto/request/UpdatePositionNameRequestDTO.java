package com.example.simple_hris.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdatePositionNameRequestDTO {
    @NotBlank(message = "Name tidak boleh kosong")
    String name;
}

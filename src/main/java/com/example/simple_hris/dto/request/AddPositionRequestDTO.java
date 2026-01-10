package com.example.simple_hris.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddPositionRequestDTO {
    @NotBlank(message = "Name tidak boleh kosong")
    private String name;
}

package com.example.simple_hris.dto.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class PositionResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String positionName;
}

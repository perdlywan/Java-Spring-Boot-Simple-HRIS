package com.example.simple_hris.dto.response;

import java.time.LocalDate;

public interface UserEmployeeResponseDTO {

    String getUsername();
    String getRole();

    String getFirstName();
    String getMiddleName();
    String getLastName();

    LocalDate getHireDate();
    String getPlaceOfBirth();
    LocalDate getDateOfBirth();

    String getReligion();
    String getMaritalStatus();
}

package com.example.simple_hris.repository;

import com.example.simple_hris.dto.response.UserEmployeeResponseDTO;
import com.example.simple_hris.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query(value = """
    SELECT 
        u.username AS username,
        u.role AS role,
        e.first_name AS firstName,
        e.middle_name AS middleName,
        e.last_name AS lastName,
        e.hire_date AS hireDate,
        e.place_of_birth AS placeOfBirth,
        e.date_of_birth AS dateOfBirth,
        e.religion AS religion,
        e.marital_status AS maritalStatus
    FROM users u
    JOIN employees e ON e.user_id = u.id
""", nativeQuery = true)
    List<UserEmployeeResponseDTO> findAllEmployeeData();
}

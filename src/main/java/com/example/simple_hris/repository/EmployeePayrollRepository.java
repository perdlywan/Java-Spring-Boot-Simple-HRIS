package com.example.simple_hris.repository;

import com.example.simple_hris.entity.Employee;
import com.example.simple_hris.entity.EmployeePayroll;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeePayrollRepository extends JpaRepository<EmployeePayroll, Long> {
    Optional<EmployeePayroll> findByEmployee(Employee employee);
}

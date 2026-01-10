package com.example.simple_hris.repository;

import com.example.simple_hris.entity.Employee;
import com.example.simple_hris.entity.EmployeeCareer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeCareerRepository extends JpaRepository<EmployeeCareer, Long> {
    List<EmployeeCareer> findAllByEmployee(Employee employee);
}

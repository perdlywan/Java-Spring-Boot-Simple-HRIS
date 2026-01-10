package com.example.simple_hris.controller;

import com.example.simple_hris.dto.request.*;
import com.example.simple_hris.dto.response.UserEmployeeResponseDTO;
import com.example.simple_hris.service.EmployeeService;
import com.example.simple_hris.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;
    private final UserService userService;

    @PostMapping
    public void addEmployee(@Valid @RequestBody AddEmployeeRequestDTO request) {
        employeeService.addEmployee(request);
    }

    @PatchMapping("salary/{id}")
    public void updateEmployeeSalary(@PathVariable("id") Long id, @RequestBody UpdateEmployeeSalaryRequestDTO request){
        employeeService.updateEmployeeSalary(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable("id") Long id){
        employeeService.deleteEmployee(id);
    }

    @PatchMapping("/personal/{id}")
    public void updateEmployeePersonal (@PathVariable("id") Long id, @RequestBody UpdateEmployeePersonalRequestDTO request){
        employeeService.updateEmployeePersonalData(id, request);
    }

    @GetMapping
    public List<UserEmployeeResponseDTO> getAllEmployees() {
        return employeeService.getAllEmployeeData();
    }
}

package com.example.simple_hris.service;

import com.example.simple_hris.dto.request.*;
import com.example.simple_hris.dto.response.UserEmployeeResponseDTO;
import com.example.simple_hris.entity.*;
import com.example.simple_hris.exception.custom.DataNotFoundException;
import com.example.simple_hris.exception.custom.BusinessException;
import com.example.simple_hris.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeCareerRepository employeeCareerRepository;
    private final EmployeePayrollRepository employeePayrollRepository;
    private final PositionRepository positionRepository;

    @Transactional
    @PreAuthorize("hasAnyRole('SUPERADMIN','HRADMIN')")
    public void addEmployee(AddEmployeeRequestDTO request) {
        Optional<User> checkUser = userRepository.findByUsername(request.getUsername());

        if(checkUser.isPresent()){
            throw new BusinessException(("Username sudah terdaftar"));
        }

        Position position = positionRepository.findById(request.getPositionId())
                .orElseThrow(() -> new DataNotFoundException("position_id tidak terdaftar"));


        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        Employee employee = new Employee();
        employee.setFirstName(request.getFirstName());
        employee.setMiddleName(request.getMiddleName());
        employee.setLastName(request.getLastName());
        employee.setHireDate(request.getHireDate());
        employee.setPlaceOfBirth(request.getPlaceOfBirth());
        employee.setDateOfBirth(request.getDateOfBirth());
        employee.setReligion(request.getReligion());
        employee.setMaritalStatus(request.getMaritalStatus());

        EmployeeCareer employeeCareer = new EmployeeCareer();
        employeeCareer.setPosition(position);
        employeeCareer.setEffectiveDate(request.getHireDate());
        employeeCareer.setEndDate(request.getEndDate());
        employeeCareer.setEmploymentStatus(request.getEmploymentStatus());
        employeeCareer.setJobDescription(request.getJobDescription());

        EmployeePayroll employeePayroll = new EmployeePayroll();
        employeePayroll.setSalary(BigDecimal.valueOf(0));

        employee.setUser(user);
        employeeCareer.setEmployee(employee);
        employeePayroll.setEmployee(employee);

        userRepository.save(user);
        employeeRepository.save(employee);
        employeeCareerRepository.save(employeeCareer);
        employeePayrollRepository.save(employeePayroll);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SUPERADMIN','PAYMASTER')")
    public void updateEmployeeSalary(Long id, UpdateEmployeeSalaryRequestDTO request){
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Employee tidak terdaftar"));

        EmployeePayroll payroll = employeePayrollRepository
                .findByEmployee(employee)
                .orElseThrow(() -> new DataNotFoundException("Payroll employee belum ada"));

        payroll.setSalary(request.getSalary());
        employeePayrollRepository.save(payroll);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SUPERADMIN','HRADMIN')")
    public void deleteEmployee(Long id){
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Employee tidak terdaftar"));

        // Delete data payroll
        EmployeePayroll payroll = employeePayrollRepository
                .findByEmployee(employee)
                .orElseThrow(() -> new DataNotFoundException("Payroll employee belum ada"));

        employeePayrollRepository.delete(payroll);

        // Delete data career
        List<EmployeeCareer> careers = employeeCareerRepository
                .findAllByEmployee(employee);
        employeeCareerRepository.deleteAll(careers);

        // Delete data user
        User user = employee.getUser();
        employeeRepository.delete(employee);
        userRepository.delete(user);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SUPERADMIN','HRADMIN')")
    public void updateEmployeePersonalData(Long id, UpdateEmployeePersonalRequestDTO request){
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Employee tidak terdaftar"));

        updateIfPresent(request.getFirstName(), employee::setFirstName);
        updateIfPresent(request.getMiddleName(), employee::setMiddleName);
        updateIfPresent(request.getLastName(), employee::setLastName);
        updateIfPresent(request.getPlaceOfBirth(), employee::setPlaceOfBirth);

        if (request.getDateOfBirth() != null) {
            employee.setDateOfBirth(request.getDateOfBirth());
        }

        if (request.getReligion() != null) {
            employee.setReligion(request.getReligion());
        }

        if (request.getMaritalStatus() != null) {
            employee.setMaritalStatus(request.getMaritalStatus());
        }

        employeeRepository.save(employee);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN','HRADMIN')")
    public List<UserEmployeeResponseDTO> getAllEmployeeData() {
        return employeeRepository.findAllEmployeeData();
    }

    private void updateIfPresent(String value, Consumer<String> setter) {
        if (value != null && !value.isBlank()) {
            setter.accept(value);
        }
    }

}

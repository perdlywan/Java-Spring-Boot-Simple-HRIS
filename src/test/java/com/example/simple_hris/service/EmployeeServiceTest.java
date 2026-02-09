package com.example.simple_hris.service;

import com.example.simple_hris.dto.request.AddEmployeeRequestDTO;
import com.example.simple_hris.dto.request.UpdateEmployeePersonalRequestDTO;
import com.example.simple_hris.dto.request.UpdateEmployeeSalaryRequestDTO;
import com.example.simple_hris.entity.*;
import com.example.simple_hris.enums.*;
import com.example.simple_hris.exception.custom.BusinessException;
import com.example.simple_hris.exception.custom.DataNotFoundException;
import com.example.simple_hris.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private PasswordEncoder encoder;
    @Mock
    private UserRepository userRepository;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private EmployeeCareerRepository employeeCareerRepository;
    @Mock
    private EmployeePayrollRepository employeePayrollRepository;
    @Mock
    private PositionRepository positionRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;
    private User user;
    private Position position;
    private AddEmployeeRequestDTO addRequest;
    private EmployeePayroll payroll;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("jdoe");
        user.setPassword("encoded");

        position = new Position();
        position.setId(1L);
        position.setName("Developer");

        employee = new Employee();
        employee.setId(1L);
        employee.setUser(user);
        employee.setFirstName("John");
        employee.setLastName("Doe");

        payroll = new EmployeePayroll();
        payroll.setId(1L);
        payroll.setEmployee(employee);
        payroll.setSalary(BigDecimal.valueOf(5000));

        addRequest = new AddEmployeeRequestDTO();
        addRequest.setUsername("jdoe");
        addRequest.setPassword("password123");
        addRequest.setRole(Role.EMPLOYEE);
        addRequest.setFirstName("John");
        addRequest.setLastName("Doe");
        addRequest.setHireDate(LocalDate.now());
        addRequest.setPlaceOfBirth("Jakarta");
        addRequest.setDateOfBirth(LocalDate.of(1990, 1, 1));
        addRequest.setReligion(Religion.ISLAM);
        addRequest.setMaritalStatus(MaritalStatus.SINGLE);
        addRequest.setPositionId(1L);
        addRequest.setEmploymentStatus(EmploymentStatus.PERMANENT);
    }

    @Test
    void addEmployee_shouldSaveAllEntities_whenRequestIsValidAndUsernameIsUnique() {
        when(userRepository.findByUsername(addRequest.getUsername())).thenReturn(Optional.empty());
        when(positionRepository.findById(addRequest.getPositionId())).thenReturn(Optional.of(position));
        when(encoder.encode(anyString())).thenReturn("encoded");

        employeeService.addEmployee(addRequest);

        verify(userRepository, times(1)).save(any(User.class));
        verify(employeeRepository, times(1)).save(any(Employee.class));
        verify(employeeCareerRepository, times(1)).save(any(EmployeeCareer.class));
        verify(employeePayrollRepository, times(1)).save(any(EmployeePayroll.class));
    }

    @Test
    void addEmployee_shouldThrowBusinessException_whenUsernameAlreadyExists() {
        when(userRepository.findByUsername(addRequest.getUsername())).thenReturn(Optional.of(user));

        assertThrows(BusinessException.class, () -> employeeService.addEmployee(addRequest));
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void addEmployee_shouldThrowDataNotFoundException_whenPositionIdInvalid() {
        when(userRepository.findByUsername(addRequest.getUsername())).thenReturn(Optional.empty());
        when(positionRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> employeeService.addEmployee(addRequest));
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void updateEmployeeSalary_shouldUpdateSalary_whenEmployeeAndPayrollExist() {
        UpdateEmployeeSalaryRequestDTO salaryRequest = new UpdateEmployeeSalaryRequestDTO();
        salaryRequest.setSalary(BigDecimal.valueOf(6000));

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeePayrollRepository.findByEmployee(employee)).thenReturn(Optional.of(payroll));

        employeeService.updateEmployeeSalary(1L, salaryRequest);

        assertEquals(BigDecimal.valueOf(6000), payroll.getSalary());
        verify(employeePayrollRepository, times(1)).save(payroll);
    }

    @Test
    void updateEmployeeSalary_shouldThrowDataNotFoundException_whenEmployeeNotFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class,
                () -> employeeService.updateEmployeeSalary(1L, new UpdateEmployeeSalaryRequestDTO()));
    }

    @Test
    void deleteEmployee_shouldDeleteAllRelatedEntities_whenEmployeeExists() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeePayrollRepository.findByEmployee(employee)).thenReturn(Optional.of(payroll));
        when(employeeCareerRepository.findAllByEmployee(employee)).thenReturn(Collections.emptyList());

        employeeService.deleteEmployee(1L);

        verify(employeePayrollRepository, times(1)).delete(payroll);
        verify(employeeRepository, times(1)).delete(employee);
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void updateEmployeePersonalData_shouldUpdateFields_whenEmployeeExists() {
        UpdateEmployeePersonalRequestDTO personalRequest = new UpdateEmployeePersonalRequestDTO();
        personalRequest.setFirstName("Johnny");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        employeeService.updateEmployeePersonalData(1L, personalRequest);

        assertEquals("Johnny", employee.getFirstName());
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    void getAllEmployeeData_shouldReturnListOfEmployees_whenDataFound() {
        when(employeeRepository.findAllEmployeeData()).thenReturn(Collections.emptyList());

        employeeService.getAllEmployeeData();

        verify(employeeRepository, times(1)).findAllEmployeeData();
    }
}

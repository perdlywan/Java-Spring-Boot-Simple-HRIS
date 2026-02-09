package com.example.simple_hris.controller;

import com.example.simple_hris.dto.request.AddEmployeeRequestDTO;
import com.example.simple_hris.dto.request.UpdateEmployeePersonalRequestDTO;
import com.example.simple_hris.dto.request.UpdateEmployeeSalaryRequestDTO;
import com.example.simple_hris.entity.Employee;
import com.example.simple_hris.entity.Position;
import com.example.simple_hris.enums.*;
import com.example.simple_hris.repository.EmployeeRepository;
import com.example.simple_hris.repository.PositionRepository;
import com.example.simple_hris.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class EmployeeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PositionRepository positionRepository;

    private Position testPosition;

    @BeforeEach
    void setUp() {
        Position position = new Position();
        position.setName("Software Engineer");
        testPosition = positionRepository.save(position);
    }

    // Add Employee
    @Test
    @WithMockUser(username = "admin", roles = "SUPERADMIN")
    void addEmployee_shouldReturn200_whenRequestIsValid() throws Exception {
        AddEmployeeRequestDTO request = createValidAddEmployeeRequest();

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        assertThat(employeeRepository.findAll()).hasSize(1);
        assertThat(userRepository.findByUsername("johndoe")).isPresent();
    }


    // Get All Employee
    @Test
    @WithMockUser(roles = "SUPERADMIN")
    void getAllEmployees_shouldReturn200_whenAuthorized() throws Exception {
        createEmployeeViaApi();

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].firstName").value("John"));
    }


    // Update Salary
    @Test
    @WithMockUser(roles = { "PAYMASTER", "SUPERADMIN" })
    void updateEmployeeSalary_shouldReturn200_whenRequestIsValid() throws Exception {
        Long employeeId = createEmployeeViaApi();

        UpdateEmployeeSalaryRequestDTO request = new UpdateEmployeeSalaryRequestDTO();
        request.setSalary(BigDecimal.valueOf(15_000_000));

        mockMvc.perform(patch("/employees/salary/{id}", employeeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    // Update Personal Data
    @Test
    @WithMockUser(roles = "HRADMIN")
    void updateEmployeePersonal_shouldReturn200_whenRequestIsValid() throws Exception {
        Long employeeId = createEmployeeViaApi();

        UpdateEmployeePersonalRequestDTO request = new UpdateEmployeePersonalRequestDTO();
        request.setFirstName("Johnny");

        mockMvc.perform(patch("/employees/personal/{id}", employeeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        Employee updated = employeeRepository.findById(employeeId).orElseThrow();
        assertThat(updated.getFirstName()).isEqualTo("Johnny");
    }

    // Delete Employee
    @Test
    @WithMockUser(roles = "SUPERADMIN")
    void deleteEmployee_shouldReturn200_whenEmployeeExists() throws Exception {
        Long employeeId = createEmployeeViaApi();

        mockMvc.perform(delete("/employees/{id}", employeeId))
                .andExpect(status().isOk());

        assertThat(employeeRepository.existsById(employeeId)).isFalse();
    }


    private AddEmployeeRequestDTO createValidAddEmployeeRequest() {
        AddEmployeeRequestDTO request = new AddEmployeeRequestDTO();
        request.setUsername("johndoe");
        request.setPassword("password123");
        request.setRole(Role.EMPLOYEE);
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setHireDate(LocalDate.now());
        request.setPlaceOfBirth("Jakarta");
        request.setDateOfBirth(LocalDate.of(1995, 1, 1));
        request.setReligion(Religion.ISLAM);
        request.setMaritalStatus(MaritalStatus.SINGLE);
        request.setPositionId(testPosition.getId());
        request.setEmploymentStatus(EmploymentStatus.PERMANENT);
        request.setSalary(BigDecimal.valueOf(10_000_000));
        return request;
    }

    private Long createEmployeeViaApi() throws Exception {
        AddEmployeeRequestDTO request = createValidAddEmployeeRequest();

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        return employeeRepository.findAll().get(0).getId();
    }
}

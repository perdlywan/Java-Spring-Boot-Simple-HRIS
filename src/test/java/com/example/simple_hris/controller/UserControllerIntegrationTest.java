package com.example.simple_hris.controller;

import com.example.simple_hris.dto.request.UpdateUserRequestDTO;
import com.example.simple_hris.dto.request.UpdateUserRoleRequestDTO;
import com.example.simple_hris.entity.User;
import com.example.simple_hris.enums.Role;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setRole(Role.EMPLOYEE);

        testUser = userRepository.save(user);
    }

    @Test
    @WithMockUser(roles = "SUPERADMIN")
    void updateUser_shouldReturn200_whenRequestIsValid() throws Exception {
        UpdateUserRequestDTO request = new UpdateUserRequestDTO();
        request.setUsername("updateduser");

        mockMvc.perform(patch("/users/{id}", testUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        User updated = userRepository.findById(testUser.getId()).orElseThrow();
        assertThat(updated.getUsername()).isEqualTo("updateduser");
    }

    @Test
    @WithMockUser(roles = "SUPERADMIN")
    void updateUserRole_shouldReturn200_whenRequestIsValid() throws Exception {
        UpdateUserRoleRequestDTO request = new UpdateUserRoleRequestDTO();
        request.setRole(Role.HRADMIN);

        mockMvc.perform(patch("/users/role/{id}", testUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        User updated = userRepository.findById(testUser.getId()).orElseThrow();
        assertThat(updated.getRole()).isEqualTo(Role.HRADMIN);
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void updateUserRole_shouldReturn403_whenNotAuthorized() throws Exception {
        UpdateUserRoleRequestDTO request = new UpdateUserRoleRequestDTO();
        request.setRole(Role.HRADMIN);

        mockMvc.perform(patch("/users/role/{id}", testUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }
}

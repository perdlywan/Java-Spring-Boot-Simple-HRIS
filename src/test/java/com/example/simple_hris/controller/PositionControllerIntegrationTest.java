package com.example.simple_hris.controller;

import com.example.simple_hris.dto.request.AddPositionRequestDTO;
import com.example.simple_hris.dto.request.UpdatePositionNameRequestDTO;
import com.example.simple_hris.entity.Position;
import com.example.simple_hris.repository.PositionRepository;
import com.example.simple_hris.service.RedisService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PositionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private RedisService redisService;

    @BeforeEach
    void setUp() {
        positionRepository.deleteAll();
        redisService.delete("position::");
    }

    // Add Position
    @Test
    @WithMockUser(roles = "SUPERADMIN")
    void addPosition_shouldReturn200_whenRequestIsValid() throws Exception {
        AddPositionRequestDTO request = new AddPositionRequestDTO();
        request.setName("Software Engineer");

        mockMvc.perform(post("/positions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        assertThat(positionRepository.findAll()).hasSize(1);
        assertThat(positionRepository.findAll().get(0).getName())
                .isEqualTo("Software Engineer");
    }

    // Get Position By Id
    @Test
    @WithMockUser(roles = "SUPERADMIN")
    void getPositionById_shouldReturn200_whenPositionExists() throws Exception {
        Long positionId = createPositionViaApi();

        mockMvc.perform(get("/positions/{id}", positionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.positionName").value("Software Engineer"));
    }

    // Update Position
    @Test
    @WithMockUser(roles = "SUPERADMIN")
    void updatePositionName_shouldReturn200_whenRequestIsValid() throws Exception {
        Position position = savePosition("Old Name");

        UpdatePositionNameRequestDTO request = new UpdatePositionNameRequestDTO();
        request.setName("New Name");

        mockMvc.perform(patch("/positions/{id}", position.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        Position updated = positionRepository.findById(position.getId()).orElseThrow();
        assertThat(updated.getName()).isEqualTo("New Name");
    }

    // Delete Position
    @Test
    @WithMockUser(roles = "SUPERADMIN")
    void deletePosition_shouldReturn200_whenPositionExists() throws Exception {
        Position position = savePosition("To Be Deleted");

        mockMvc.perform(delete("/positions/{id}", position.getId()))
                .andExpect(status().isOk());

        assertThat(positionRepository.existsById(position.getId())).isFalse();
    }

    private Position savePosition(String name) {
        Position position = new Position();
        position.setName(name);
        return positionRepository.save(position);
    }

    private Long createPositionViaApi() throws Exception {
        AddPositionRequestDTO request = new AddPositionRequestDTO();
        request.setName("Software Engineer");

        mockMvc.perform(post("/positions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        return positionRepository.findByName("Software Engineer")
                .orElseThrow()
                .getId();
    }

}

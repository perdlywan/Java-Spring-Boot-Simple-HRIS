package com.example.simple_hris.service;

import com.example.simple_hris.dto.request.AddPositionRequestDTO;
import com.example.simple_hris.dto.request.UpdatePositionNameRequestDTO;
import com.example.simple_hris.dto.response.PositionResponseDTO;
import com.example.simple_hris.entity.Position;
import com.example.simple_hris.exception.custom.BusinessException;
import com.example.simple_hris.exception.custom.DataNotFoundException;
import com.example.simple_hris.repository.PositionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PositionServiceTest {

    @Mock
    private RedisService redisService;

    @Mock
    private PositionRepository positionRepository;

    @InjectMocks
    private PositionService positionService;

    private Position position;
    private AddPositionRequestDTO addRequest;
    private UpdatePositionNameRequestDTO updateRequest;

    @BeforeEach
    void setUp() {
        position = new Position();
        position.setId(1L);
        position.setName("Developer");

        addRequest = new AddPositionRequestDTO();
        addRequest.setName("Developer");

        updateRequest = new UpdatePositionNameRequestDTO();
        updateRequest.setName("Senior Developer");
    }

    @Test
    void addPosition_shouldSavePosition_whenNameIsUnique() {
        when(positionRepository.findByName(addRequest.getName())).thenReturn(Optional.empty());

        positionService.addPosition(addRequest);

        verify(positionRepository, times(1)).save(any(Position.class));
    }

    @Test
    void addPosition_shouldThrowBusinessException_whenNameAlreadyExists() {
        when(positionRepository.findByName(addRequest.getName())).thenReturn(Optional.of(position));

        assertThrows(BusinessException.class, () -> positionService.addPosition(addRequest));
        verify(positionRepository, never()).save(any(Position.class));
    }

    @Test
    void updatePositionName_shouldUpdateNameAndEvictCache_whenUserExistsAndNameIsUnique() {
        when(positionRepository.findById(1L)).thenReturn(Optional.of(position));
        when(positionRepository.findByName(updateRequest.getName())).thenReturn(Optional.empty());

        positionService.updatePositionName(1L, updateRequest);

        assertEquals("Senior Developer", position.getName());
        verify(positionRepository, times(1)).save(position);
        verify(redisService, times(1)).delete("position::1");
    }

    @Test
    void updatePositionName_shouldThrowDataNotFoundException_whenPositionNotFound() {
        when(positionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> positionService.updatePositionName(1L, updateRequest));
        verify(positionRepository, never()).save(any(Position.class));
    }

    @Test
    void updatePositionName_shouldThrowBusinessException_whenNameAlreadyExists() {
        when(positionRepository.findById(1L)).thenReturn(Optional.of(position));
        when(positionRepository.findByName(updateRequest.getName())).thenReturn(Optional.of(new Position()));

        assertThrows(BusinessException.class, () -> positionService.updatePositionName(1L, updateRequest));
        verify(positionRepository, never()).save(any(Position.class));
    }

    @Test
    void deletePosition_shouldDeleteAndEvictCache_whenPositionExists() {
        when(positionRepository.findById(1L)).thenReturn(Optional.of(position));

        positionService.deletePosition(1L);

        verify(positionRepository, times(1)).delete(position);
        verify(redisService, times(1)).delete("position::1");
    }

    @Test
    void deletePosition_shouldThrowDataNotFoundException_whenPositionNotFound() {
        when(positionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> positionService.deletePosition(1L));
        verify(positionRepository, never()).delete(any(Position.class));
    }

    @Test
    void getPositionById_shouldReturnFromRedis_whenCacheHit() {
        PositionResponseDTO cachedRes = new PositionResponseDTO();
        cachedRes.setPositionName("Developer");
        when(redisService.get("position::1", PositionResponseDTO.class)).thenReturn(cachedRes);

        PositionResponseDTO result = positionService.getPositionById(1L);

        assertNotNull(result);
        assertEquals("Developer", result.getPositionName());
        verify(positionRepository, never()).findById(anyLong());
    }

    @Test
    void getPositionById_shouldReturnFromDbAndSetRedis_whenCacheMiss() {
        when(redisService.get("position::1", PositionResponseDTO.class)).thenReturn(null);
        when(positionRepository.findById(1L)).thenReturn(Optional.of(position));

        PositionResponseDTO result = positionService.getPositionById(1L);

        assertNotNull(result);
        assertEquals("Developer", result.getPositionName());
        verify(redisService, times(1)).set(eq("position::1"), any(PositionResponseDTO.class), any(Duration.class));
    }
}

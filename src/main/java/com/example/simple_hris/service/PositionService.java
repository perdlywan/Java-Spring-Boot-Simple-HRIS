package com.example.simple_hris.service;

import com.example.simple_hris.dto.request.AddPositionRequestDTO;
import com.example.simple_hris.dto.request.UpdatePositionNameRequestDTO;
import com.example.simple_hris.dto.response.PositionResponseDTO;
import com.example.simple_hris.entity.Position;
import com.example.simple_hris.entity.User;
import com.example.simple_hris.exception.custom.BusinessException;
import com.example.simple_hris.exception.custom.DataNotFoundException;
import com.example.simple_hris.repository.PositionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.CacheManager;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PositionService {
    private final RedisService redisService;
    private static final String POSITION_CACHE_PREFIX = "position::";

    private final CacheManager cacheManager;

    private final PositionRepository positionRepository;

    @Transactional
    @PreAuthorize("hasAnyRole('SUPERADMIN','HRADMIN')")
    public void addPosition(AddPositionRequestDTO request) {
        Optional<Position> checkPosName = positionRepository.findByName(request.getName());

        if(checkPosName.isPresent()){
            throw new BusinessException(("Positon name sudah terdaftar"));
        }

        Position position = new Position();
        position.setName(request.getName());
        positionRepository.save(position);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SUPERADMIN','HRADMIN')")
    public void updatePositionName(Long id, UpdatePositionNameRequestDTO request){
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Position tidak ditemukan"));

        Optional<Position> checkPosName = positionRepository.findByName(request.getName());

        if(checkPosName.isPresent()){
            throw new BusinessException(("Positon name sudah terdaftar"));
        }

        position.setName(request.getName());
        positionRepository.save(position);

        // Evict CACHE
        redisService.delete(POSITION_CACHE_PREFIX + id);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SUPERADMIN','HRADMIN')")
    public void deletePosition(Long id){
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Position tidak ditemukan"));

        positionRepository.delete(position);
        // Evict CACHE
        redisService.delete(POSITION_CACHE_PREFIX + id);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN','HRADMIN')")
    public PositionResponseDTO getPositionById(Long id){
        try {
            PositionResponseDTO value = redisService.get(POSITION_CACHE_PREFIX + id, PositionResponseDTO.class);

            if (Objects.nonNull(value)) {
                log.info("get from redis");
                return value;
            }

        } catch (Exception e) {
            log.error("error in redis", e);
        }

        log.info("read from database");

        return positionRepository.findById(id)
                .map(data -> {
                    PositionResponseDTO res = new PositionResponseDTO();
                    res.setPositionName(data.getName());
                    redisService.set(
                            POSITION_CACHE_PREFIX + id,
                            res,
                            Duration.ofMinutes(5)
                    );

                    return res;
                })
                .orElse(null);
    }
}

package com.example.simple_hris.repository;

import com.example.simple_hris.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PositionRepository extends JpaRepository<Position, Long> {
    Optional<Position> findById(Long id);
    Optional<Position> findByName(String name);
}

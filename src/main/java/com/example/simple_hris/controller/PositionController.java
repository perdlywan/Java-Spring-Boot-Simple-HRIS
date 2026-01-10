package com.example.simple_hris.controller;

import com.example.simple_hris.dto.request.AddPositionRequestDTO;
import com.example.simple_hris.dto.request.UpdatePositionNameRequestDTO;
import com.example.simple_hris.dto.response.PositionResponseDTO;
import com.example.simple_hris.service.PositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/positions")
@RequiredArgsConstructor
public class PositionController {
    private final PositionService positionService;
    @PostMapping
    public void addPosition(@RequestBody AddPositionRequestDTO request) {
        positionService.addPosition(request);
    }

    @PatchMapping("/{id}")
    public void updatePositionName(@PathVariable("id") Long id, @RequestBody UpdatePositionNameRequestDTO request){
        positionService.updatePositionName(id, request);
    }

    @DeleteMapping("/{id}")
    public void deletePosition(@PathVariable("id") Long id){
        positionService.deletePosition(id);
    }

    @GetMapping("/{id}")
    public PositionResponseDTO getPositionById(@PathVariable Long id) {
        return positionService.getPositionById(id);
    }
}

package com.improel.samp.controller;

import com.improel.samp.dto.request.EndShiftRequestDTO;
import com.improel.samp.dto.request.StartShiftRequestDTO;
import com.improel.samp.dto.response.ShiftRecordResponseDTO;
import com.improel.samp.entity.ShiftRecord;
import com.improel.samp.service.ShiftService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shifts")
@RequiredArgsConstructor
public class ShiftController {

    private final ShiftService shiftService;

    @PostMapping("/start")
    public ResponseEntity<ShiftRecordResponseDTO> startShift(@Valid @RequestBody StartShiftRequestDTO request) {
        ShiftRecord shift = shiftService.startShift(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ShiftRecordResponseDTO.fromEntity(shift));
    }

    @PostMapping("/end")
    public ResponseEntity<ShiftRecordResponseDTO> endShift(@Valid @RequestBody EndShiftRequestDTO request) {
        ShiftRecord shift = shiftService.endShift(request);
        return ResponseEntity.ok(ShiftRecordResponseDTO.fromEntity(shift));
    }

    @GetMapping("/open/{operatorId}")
    public ResponseEntity<ShiftRecordResponseDTO> getOpenShift(@PathVariable Long operatorId) {
        return shiftService.findOpenShift(operatorId)
                .map(ShiftRecordResponseDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("/anomalies")
    public ResponseEntity<List<ShiftRecordResponseDTO>> getAnomalies() {
        List<ShiftRecordResponseDTO> anomalies = shiftService.findAnomalies()
                .stream()
                .map(ShiftRecordResponseDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(anomalies);
    }

}

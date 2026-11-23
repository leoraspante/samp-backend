// Controller responsável pela gerência de ausência dos funcionários (Exclusivo Gestor).

package com.improel.samp.controller;

import com.improel.samp.dto.request.CreateAbsenceRecordRequestDTO;
import com.improel.samp.dto.response.AbsenceRecordResponseDTO;
import com.improel.samp.entity.AbsenceRecord;
import com.improel.samp.service.AbsenceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/absences")
@RequiredArgsConstructor
public class AbsenceRecordController {

    private final AbsenceService absenceService;

    // 1. Lista todos os registros de ausência dos colaboradores (Histórico geral da fábrica).
    @GetMapping
    public ResponseEntity<List<AbsenceRecordResponseDTO>> findAll() {
        List<AbsenceRecordResponseDTO> records = absenceService.findAll().stream()
                .map(record -> AbsenceRecordResponseDTO.fromEntity(record))
                .toList();
        return ResponseEntity.ok(records);
    }

    // 2. Busca o histórico de ausências de um funcionário em específico.
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AbsenceRecordResponseDTO>> findByUserId(@PathVariable Long userId) {
        List<AbsenceRecordResponseDTO> records = absenceService.findByUser(userId).stream()
                .map(record -> AbsenceRecordResponseDTO.fromEntity(record))
                .toList();
        return ResponseEntity.ok(records);
    }

    // 3. Registra uma nova falta, atestado ou férias (Painel do Gestor).
    @PostMapping
    public ResponseEntity<AbsenceRecordResponseDTO> create(@Valid @RequestBody CreateAbsenceRecordRequestDTO request) {
        AbsenceRecord savedRecord = absenceService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(AbsenceRecordResponseDTO.fromEntity(savedRecord));
    }
}

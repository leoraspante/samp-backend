// Controller responsável pelas requisições referentes as bancadas.

package com.improel.samp.controller;

import com.improel.samp.dto.request.CreateWorkstationRequestDTO;
import com.improel.samp.dto.response.WorkstationResponseDTO;
import com.improel.samp.entity.Workstation;
import com.improel.samp.service.WorkstationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/workstations")
@RequiredArgsConstructor
public class WorkstationController {

    private final WorkstationService workstationService;

    // 1. Lista todas as bancadas cadastradas.
    @GetMapping
    public ResponseEntity<List<WorkstationResponseDTO>> findAll() {
        List<WorkstationResponseDTO> workstations = workstationService.findAll().stream().map(WorkstationResponseDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(workstations);
    }

    // 2. Busca uma bancada específica pelo seu ID.
    @GetMapping("/{id}")
    public ResponseEntity<WorkstationResponseDTO> findById(@PathVariable Long id) {
        return workstationService.findById(id)
                .map(WorkstationResponseDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()); // Retorna o erro 404, se não encontrar.
    }

    // 3. Cadastra uma nova bancada (Utilizado pelo painel do gestor).
    @PostMapping
    public ResponseEntity<WorkstationResponseDTO> create(@Valid @RequestBody CreateWorkstationRequestDTO request) {
        Workstation savedWorkstation = workstationService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(WorkstationResponseDTO.fromEntity(savedWorkstation));
    }
}

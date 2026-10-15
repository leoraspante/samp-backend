// Controller responsável pelas requisições referentes as bancadas.

package com.improel.samp.controller;

import com.improel.samp.dto.WorkstationDTO;
import com.improel.samp.entity.Workstation;
import com.improel.samp.service.WorkstationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/workstations")
public class WorkstationController {

    private final WorkstationService workstationService;

    @Autowired
    public WorkstationController(WorkstationService workstationService) {
        this.workstationService = workstationService;
    }

    // 1. Lista todas as bancadas cadastradas.
    @GetMapping
    public ResponseEntity<List<WorkstationDTO>> findAll() {
        List<WorkstationDTO> workstations = workstationService.findAll().stream().map(WorkstationDTO::fromEntity).toList();
        return ResponseEntity.ok(workstations);
    }

    // 2. Busca uma bancada específica pelo seu ID.
    @GetMapping("/{id}")
    public ResponseEntity<WorkstationDTO> findById(@PathVariable Long id) {
        return workstationService.findById(id)
                .map(WorkstationDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());     // Retorna o erro 404, se não encontrar.
    }

    // 3. Cadastra uma nova bancada (Utilizado pelo painel do gestor).
    @PostMapping
    public ResponseEntity<WorkstationDTO> create(@RequestBody Workstation workstation) {
        Workstation savedWorkstation = workstationService.save(workstation);
        return ResponseEntity.status(HttpStatus.CREATED).body(WorkstationDTO.fromEntity(savedWorkstation));
    }
}

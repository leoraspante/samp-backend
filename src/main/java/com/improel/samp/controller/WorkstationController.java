// Controller responsável pelas requisições referentes as bancadas.

package com.improel.samp.controller;

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
    // GET: http://localhost:8080/api/workstations
    @GetMapping
    public ResponseEntity<List<Workstation>> findAll() {
        List<Workstation> workstations = workstationService.findAll();
        return ResponseEntity.ok(workstations);
    }

    // 2. Busca uma bancada específica pelo seu ID.
    // GET: http://localhost:8080/api/workstations/1
    @GetMapping("/{id}")
    public ResponseEntity<Workstation> findById(@PathVariable Long id) {
        return workstationService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());     // Retorna o erro 404, se não encontrar.
    }

    // 3. Cadastra uma nova bancada (Utilizado pelo painel do gestor).
    // POST: http://localhost:8080/api/workstations
    @PostMapping
    public ResponseEntity<Workstation> create(@RequestBody Workstation workstation) {
        // O @RequestBody pega o JSON enviado na requisição e transforma no objeto Workstation.
        Workstation savedWorkstation = workstationService.save(workstation);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedWorkstation);    // Retorna 201 Created.
    }
}

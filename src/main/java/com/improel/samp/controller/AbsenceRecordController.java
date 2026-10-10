// Controller responsável pela gerência de ausência dos funcionários (Exclusivo Gestor).

package com.improel.samp.controller;

import com.improel.samp.entity.AbsenceRecord;
import com.improel.samp.service.AbsenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/absences")
public class AbsenceRecordController {

    private final AbsenceService absenceService;

    @Autowired
    public AbsenceRecordController(AbsenceService absenceService) {
        this.absenceService = absenceService;
    }

    // 1. Lista todos os registros de ausência dos colaboradores (Histórico geral da fábrica).
    // GET: http://localhost:8080/api/absences
    @GetMapping
    public ResponseEntity<List<AbsenceRecord>> findAll() {
        return ResponseEntity.ok(absenceService.findAll());
    }

    // 2. Busca o histórico de ausências de um funcionário em específico.
    // GET: http://localhost:8080/api/absences/user/1
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AbsenceRecord>> findByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(absenceService.findByUser(userId));
    }

    // 3. Registra uma nova falta, atestado ou férias (Painel do Gestor).
    // POST: http://localhost:8080/api/absences
    @PostMapping
    public ResponseEntity<?> create(@RequestBody AbsenceRecord absenceRecord) {
        try {
            AbsenceRecord savedRecord = absenceService.save(absenceRecord);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRecord);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

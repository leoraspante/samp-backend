// Controller responsável pela gerência dos logs das Ordens de Produção.

package com.improel.samp.controller;

import com.improel.samp.dto.*;
import com.improel.samp.entity.ProductionLog;
import com.improel.samp.service.ProductionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
public class ProductionLogController {

    private final ProductionLogService productionLogService;

    @Autowired
    public ProductionLogController(ProductionLogService productionLogService) {
        this.productionLogService = productionLogService;
    }

    // ----------------------------------------------------------------------------------------------------------------------------------------------------------------
    // MÉTODOS DO TABLET (OPERAÇÃO NO CHÃO DE FÁBRICA)
    // ----------------------------------------------------------------------------------------------------------------------------------------------------------------

    // 1. Inicia a produção de um lote.
    @PostMapping("/start")
    public ResponseEntity<?> startProduction(@RequestBody StartProductionRequest request) {
        try {
            ProductionLog log = productionLogService.startProduction(request.productionOrderId(), request.workstationId(), request.operatorId());
            return ResponseEntity.status(HttpStatus.CREATED).body(log);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 2. Pausa a produção.
    @PostMapping("/pause")
    public ResponseEntity<?> pauseProduction(@RequestBody PauseProductionRequest request) {
        try {
            ProductionLog log = productionLogService.pauseProduction(request.workstationId(), request.reason(), request.remarks());
            return ResponseEntity.ok(log);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 3. Retoma a produção.
    @PostMapping("/resume")
    public ResponseEntity<?> resumeProduction(@RequestBody WorkstationActionRequest request) {
        try {
            ProductionLog log = productionLogService.resumeProduction(request.workstationId());
            return ResponseEntity.ok(log);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 4. Finaliza a produção do lote na bancada.
    @PostMapping("/complete")
    public ResponseEntity<?> completeProduction(@RequestBody WorkstationActionRequest request) {
        try {
            ProductionLog log = productionLogService.completeProduction(request.workstationId());
            return ResponseEntity.ok(log);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 5. Recebe os apontamentos retidos no tablet (Modo Offline).
    @PostMapping("/sync")
    public ResponseEntity<?> syncOfflineLogs(@RequestBody List<OfflineLogSyncDTO> offlineLogs) {
        try {
            productionLogService.syncOfflineLogs(offlineLogs);
            return ResponseEntity.ok().body("Sincronização offline concluída com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro na sincronização: " + e.getMessage());
        }
    }

    // ----------------------------------------------------------------------------------------------------------------------------------------------------------------
    // MÉTODOS DO GESTOR (HANDOVER)
    // ----------------------------------------------------------------------------------------------------------------------------------------------------------------

    // 6. Liberta a Ordem de Serviço em andamento (Operador 1 deixa o serviço em que estava atualmente trabalhando).
    @PostMapping("/handover/release")
    public ResponseEntity<?> handoverRelease(@RequestBody HandoverReleaseRequest request) {
        try {
            ProductionLog log = productionLogService.manageHandoverRelease(request.workstationId(), request.partialQuantity(), request.remarks());
            return ResponseEntity.ok(log);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 7. Assume a Ordem de Serviço em andamento (Operador 2 assume o serviço em que o Operador 1 estava trabalhando).
    @PostMapping("/handover/take")
    public ResponseEntity<?> handoverTake(@RequestBody HandoverTakeRequest request) {
        try {
            ProductionLog log = productionLogService.manageHandoverTake(request.orderId(), request.workstationId(), request.newOperatorId(), request.previousOperatorId());
            return ResponseEntity.ok(log);
        }  catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

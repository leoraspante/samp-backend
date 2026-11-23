// Controller responsável pela gerência dos logs das Ordens de Produção.

package com.improel.samp.controller;

import com.improel.samp.dto.request.*;
import com.improel.samp.dto.response.ProductionLogResponseDTO;
import com.improel.samp.entity.ProductionLog;
import com.improel.samp.service.ProductionLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class ProductionLogController {

    private final ProductionLogService productionLogService;

    // ----------------------------------------------------------------------------------------------------------------------------------------------------------------
    // MÉTODOS DE CONSULTA (HISTÓRICO E LINHA DO TEMPO)
    // ----------------------------------------------------------------------------------------------------------------------------------------------------------------

    @GetMapping
    public ResponseEntity<List<ProductionLogResponseDTO>> findAll() {
        List<ProductionLogResponseDTO> logs = productionLogService.findAll().stream()
                .map(ProductionLogResponseDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductionLogResponseDTO> findById(@PathVariable Long id) {
        return productionLogService.findById(id)
                .map(ProductionLogResponseDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ----------------------------------------------------------------------------------------------------------------------------------------------------------------
    // MÉTODOS DO TABLET (OPERAÇÃO NO CHÃO DE FÁBRICA)
    // ----------------------------------------------------------------------------------------------------------------------------------------------------------------

    // 1. Inicia a produção de um lote.
    @PostMapping("/start")
    public ResponseEntity<ProductionLogResponseDTO> startProduction(@RequestBody StartProductionRequestDTO request) {
        ProductionLog log = productionLogService.startProduction(request.productionOrderId(),
                request.workstationId(), request.operatorId());
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductionLogResponseDTO.fromEntity(log));
    }

    // 2. Pausa a produção.
    @PostMapping("/pause")
    public ResponseEntity<ProductionLogResponseDTO> pauseProduction(@RequestBody PauseProductionRequestDTO request) {
        ProductionLog log = productionLogService.pauseProduction(request.workstationId(), request.reason(),
                request.remarks());
        return ResponseEntity.ok(ProductionLogResponseDTO.fromEntity(log));
    }

    // 3. Retoma a produção.
    @PostMapping("/resume")
    public ResponseEntity<ProductionLogResponseDTO> resumeProduction(@RequestBody WorkstationActionRequestDTO request) {
        ProductionLog log = productionLogService.resumeProduction(request.workstationId());
        return ResponseEntity.ok(ProductionLogResponseDTO.fromEntity(log));
    }

    // 4. Finaliza a produção do lote na bancada.
    @PostMapping("/complete")
    public ResponseEntity<ProductionLogResponseDTO> completeProduction(@RequestBody WorkstationActionRequestDTO request) {
        ProductionLog log = productionLogService.completeProduction(request.workstationId());
        return ResponseEntity.ok(ProductionLogResponseDTO.fromEntity(log));
    }

    // 5. Recebe os apontamentos retidos no tablet (Modo Offline).
    @PostMapping("/sync")
    public ResponseEntity<String> syncOfflineLogs(@RequestBody List<OfflineLogSyncRequestDTO> offlineLogs) {
        productionLogService.syncOfflineLogs(offlineLogs);
        return ResponseEntity.ok("Sincronização offline concluída com sucesso!");
    }

    // ----------------------------------------------------------------------------------------------------------------------------------------------------------------
    // MÉTODOS DO GESTOR (HANDOVER E QUALIDADE / INSPEÇÃO)
    // ----------------------------------------------------------------------------------------------------------------------------------------------------------------

    // 6. Libera a Ordem de Serviço em andamento (Operador 1 deixa o serviço em que
    // estava atualmente trabalhando).
    @PostMapping("/handover/release")
    public ResponseEntity<ProductionLogResponseDTO> handoverRelease(@RequestBody HandoverReleaseRequestDTO request) {
        ProductionLog log = productionLogService.manageHandoverRelease(request.workstationId(),
                request.partialQuantity(), request.remarks());
        return ResponseEntity.ok(ProductionLogResponseDTO.fromEntity(log));
    }

    // 7. Assume a Ordem de Serviço em andamento (Operador 2 assume o serviço em que
    // o Operador 1 estava trabalhando).
    @PostMapping("/handover/take")
    public ResponseEntity<ProductionLogResponseDTO> handoverTake(@RequestBody HandoverTakeRequestDTO request) {
        ProductionLog log = productionLogService.manageHandoverTake(request.orderId(), request.workstationId(),
                request.newOperatorId(), request.previousOperatorId());
        return ResponseEntity.ok(ProductionLogResponseDTO.fromEntity(log));
    }

    // 8. Gestor aprova a montagem do lote e consolida a quantidade produzida.
    @PostMapping("/approve")
    public ResponseEntity<ProductionLogResponseDTO> approveProduction(@RequestBody ApproveProductionRequestDTO request) {
        ProductionLog log = productionLogService.approveProduction(request.productionOrderId(),
                request.approvedQuantity(), request.managerId());
        return ResponseEntity.ok(ProductionLogResponseDTO.fromEntity(log));
    }

    // 9. Gestor reprova e envia o lote para retrabalho na bancada.
    @PostMapping("/rework")
    public ResponseEntity<ProductionLogResponseDTO> reworkProduction(@RequestBody ReworkProductionRequestDTO request) {
        ProductionLog log = productionLogService.reworkProduction(request.productionOrderId(), request.notes(),
                request.managerId());
        return ResponseEntity.ok(ProductionLogResponseDTO.fromEntity(log));
    }
}

// DTO para dados de logs de produção alinhado à entidade ProductionLog.

package com.improel.samp.dto;

import com.improel.samp.domain.LogType;
import com.improel.samp.domain.PauseReason;
import com.improel.samp.entity.ProductionLog;

import java.time.LocalDateTime;

public record ProductionLogDTO(
        Long id,
        Long productionOrderId,
        Long workstationId,
        Long operatorId,
        Long transferredFromUserId,
        LogType logType,
        PauseReason pauseReason,
        LocalDateTime startTimestamp,
        LocalDateTime endTimestamp,
        Long durationSeconds,
        Integer completedQuantity,
        String remarks,
        LocalDateTime createdAt
) {
    // Converte a Entidade JPA ProductionLog para o DTO de resposta.
    public static ProductionLogDTO fromEntity(ProductionLog log) {
        return new ProductionLogDTO(
                log.getId(),
                log.getProductionOrder() != null ? log.getProductionOrder().getId() : null,
                log.getWorkstation() != null ? log.getWorkstation().getId() : null,
                log.getOperator() != null ? log.getOperator().getId() : null,
                log.getTransferredFromUser() != null ? log.getTransferredFromUser().getId() : null,
                log.getLogType(),
                log.getPauseReason(),
                log.getStartTimestamp(),
                log.getEndTimestamp(),
                log.getDurationSeconds(),
                log.getCompletedQuantity(),
                log.getRemarks(),
                log.getCreatedAt()
        );
    }
}

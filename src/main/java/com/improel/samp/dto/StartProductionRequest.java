// DTO enviado pelo tablet ao iniciar a produção de uma OP na bancada de montagem.

package com.improel.samp.dto;

public record StartProductionRequest(
        Long productionOrderId,
        Long workstationId,
        Long operatorId
) {
}

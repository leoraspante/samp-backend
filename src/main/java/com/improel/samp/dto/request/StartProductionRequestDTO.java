// DTO enviado pelo tablet ao iniciar a produção de uma OP na bancada de montagem.

package com.improel.samp.dto.request;

public record StartProductionRequestDTO(
        Long productionOrderId,
        Long workstationId,
        Long operatorId
) {
}

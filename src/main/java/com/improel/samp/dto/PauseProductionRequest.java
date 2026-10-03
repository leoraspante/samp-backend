// DTO enviado pelo tablet ao pausar a produção por algum motivo operacional.

package com.improel.samp.dto;

import com.improel.samp.domain.PauseReason;

public record PauseProductionRequest(
        Long productionOrderId,
        Long workstationId,
        Long operatorId,
        PauseReason reason,
        String remarks
) {
}

// DTO para recebimento de logs retidos no tablet durante queda de conexão.

package com.improel.samp.dto;

import com.improel.samp.domain.LogType;
import com.improel.samp.domain.PauseReason;
import java.time.LocalDateTime;

public record OfflineLogSyncDTO(
        Long productionOrderId,
        Long workstationId,
        Long operatorId,
        LogType logType,
        PauseReason pauseReason,            // Pode vir nulo se não for log de pausa.
        LocalDateTime recordedTimestamp,    // Considera o horário do tablet.
        String remarks                      // Observações ou justificativas se houver.
) {
}

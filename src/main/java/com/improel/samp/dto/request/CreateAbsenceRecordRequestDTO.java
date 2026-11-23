// DTO de entrada com validações para registrar uma nova ausência/falta de colaborador.

package com.improel.samp.dto.request;

import com.improel.samp.domain.AbsenceReason;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record CreateAbsenceRecordRequestDTO(
        @NotNull(message = "O ID do colaborador é obrigatório")
        Long userId,

        @NotNull(message = "O motivo da ausência é obrigatório")
        AbsenceReason reason,

        @NotNull(message = "A data inicial é obrigatória")
        LocalDate startDate,

        @NotNull(message = "A data final é obrigatória")
        LocalDate endDate,

        @Size(max = 255, message = "A observação deve ter no máximo 255 caracteres")
        String observation
) {
}

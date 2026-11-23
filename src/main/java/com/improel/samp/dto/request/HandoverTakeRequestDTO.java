// DTO responsável pelas trocas de produção/operador (Autorizado pelo Gestor - Assunção).

package com.improel.samp.dto.request;

public record HandoverTakeRequestDTO(
        Long orderId,
        Long workstationId,
        Long newOperatorId,
        Long previousOperatorId
) {
}

// DTO responsável pelas trocas de produção/operador (Autorizado pelo Gestor - Liberação).

package com.improel.samp.dto.request;

public record HandoverReleaseRequestDTO(
        Long workstationId,
        Integer partialQuantity,
        String remarks
) {
}

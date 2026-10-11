// DTO responsável pelas trocas de produção/operador (Autorizado pelo Gestor - Liberação).

package com.improel.samp.dto;

public record HandoverReleaseRequest(
        Long workstationId,
        Integer partialQuantity,
        String remarks
) {
}

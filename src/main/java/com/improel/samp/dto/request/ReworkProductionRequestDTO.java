// DTO responsável pelas reprovações/retrabalho de Ordens de Produção (Autorizado pelo Gestor).

package com.improel.samp.dto.request;

public record ReworkProductionRequestDTO(
        Long productionOrderId,
        String notes,
        Long managerId
) {
}

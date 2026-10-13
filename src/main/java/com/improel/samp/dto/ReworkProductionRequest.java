// DTO responsável pelas reprovações/retrabalho de Ordens de Produção (Autorizado pelo Gestor).

package com.improel.samp.dto;

public record ReworkProductionRequest(
        Long productionOrderId,
        String notes,
        Long managerId
) {
}

// DTO responsável pelas aprovações de Ordens de Produção finalizadas (Autorizado pelo Gestor).

package com.improel.samp.dto;

public record ApproveProductionRequest(
        Long productionOrderId,
        Integer approvedQuantity,
        Long managerId
) {
}

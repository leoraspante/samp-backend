// DTO responsável pelas aprovações de Ordens de Produção finalizadas (Autorizado pelo Gestor).

package com.improel.samp.dto.request;

public record ApproveProductionRequestDTO(
        Long productionOrderId,
        Integer approvedQuantity,
        Long managerId
) {
}

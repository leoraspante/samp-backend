// DTO para dados de Ordens de Produção (OP) alinhado com a Entidade ProductionOrder.

package com.improel.samp.dto.response;

import com.improel.samp.domain.OrderPriority;
import com.improel.samp.domain.OrderStatus;
import com.improel.samp.entity.ProductionOrder;
import java.time.LocalDateTime;

public record ProductionOrderResponseDTO(
        Long id,
        String orderNumber,
        ProductResponseDTO product,
        Integer targetQuantity,
        Integer producedQuantity,
        OrderStatus status,
        OrderPriority priority,
        Long totalAssemblySeconds,
        String notes,
        LocalDateTime startTime,
        LocalDateTime endTime,
        LocalDateTime dueDate,
        LocalDateTime createdAt
) {
    // Converte a entidade JPA ProductionOrder para o DTO de resposta.
    public static ProductionOrderResponseDTO fromEntity(ProductionOrder order) {
        return new ProductionOrderResponseDTO(
                order.getId(),
                order.getOrderNumber(),
                order.getProduct() != null ? ProductResponseDTO.fromEntity(order.getProduct()) : null,
                order.getTargetQuantity(),
                order.getProducedQuantity(),
                order.getStatus(),
                order.getPriority(),
                order.getTotalAssemblySeconds(),
                order.getNotes(),
                order.getStartTime(),
                order.getEndTime(),
                order.getDueDate(),
                order.getCreatedAt()
        );
    }
}

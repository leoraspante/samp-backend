// DTO para dados de Ordens de Produção (OP) alinhado com a Entidade ProductionOrder.

package com.improel.samp.dto;

import com.improel.samp.domain.OrderPriority;
import com.improel.samp.domain.OrderStatus;
import com.improel.samp.entity.ProductionOrder;
import java.time.LocalDateTime;

public record ProductionOrderDTO(
        Long id,
        String orderNumber,
        ProductDTO product,
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
    public static ProductionOrderDTO fromEntity(ProductionOrder order) {
        return new ProductionOrderDTO(
                order.getId(),
                order.getOrderNumber(),
                order.getProduct() != null ? ProductDTO.fromEntity(order.getProduct()) : null,
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

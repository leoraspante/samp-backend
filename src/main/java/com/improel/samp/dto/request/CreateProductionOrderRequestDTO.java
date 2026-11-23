package com.improel.samp.dto.request;

import com.improel.samp.domain.OrderPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record CreateProductionOrderRequestDTO(

        @NotBlank(message = "O número do pedido deve ser informado.")
        String orderNumber,

        @NotNull(message = "A ID do produto deve ser informada.")
        Long productId,

        @NotNull(message = "A quantidade deve ser informada.")
        @Positive(message = "A quantidade deve ser maior que zero.")
        Integer targetQuantity,

        @NotNull(message = "A prioridade deve ser informada.")
        OrderPriority priority,

        LocalDateTime dueDate,

        String notes
) {
}

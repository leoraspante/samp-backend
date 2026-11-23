package com.improel.samp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CreateProductRequestDTO(

        @NotBlank(message = "O código do produto deve ser informado.")
        String code,

        @NotBlank(message = "O nome do produto deve ser informado.")
        String name,

        String description,

        @Positive(message = "O tempo alvo deve ser maior que zero.")
        Integer targetCycleTimeSeconds
) {
}

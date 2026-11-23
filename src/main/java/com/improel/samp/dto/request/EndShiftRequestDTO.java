package com.improel.samp.dto.request;

import jakarta.validation.constraints.NotNull;

public record EndShiftRequestDTO(

        @NotNull(message = "O ID do operador é obrigatório.")
        Long operatorId
) {
}

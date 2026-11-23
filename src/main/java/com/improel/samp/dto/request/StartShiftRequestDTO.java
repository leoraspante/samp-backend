package com.improel.samp.dto.request;

import jakarta.validation.constraints.NotNull;

public record StartShiftRequestDTO(

        @NotNull(message = "O ID do operador é obrigatório.")
        Long operatorId,

        @NotNull(message = "O ID da bancada é obrigatório.")
        Long workstationId
) {
}

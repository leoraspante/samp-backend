package com.improel.samp.dto.request;


import com.improel.samp.domain.WorkstationStatus;
import jakarta.validation.constraints.NotBlank;

public record CreateWorkstationRequestDTO(

        @NotBlank(message = "O nome da workstation deve ser informado.")
        String name,

        WorkstationStatus status,

        String tabletUuid
) {
}

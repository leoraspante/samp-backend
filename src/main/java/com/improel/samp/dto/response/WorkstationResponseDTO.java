// DTO de resposta para trafegar dados das bancadas de montagem sem expor a Entidade JPA direta.

package com.improel.samp.dto.response;

import com.improel.samp.domain.WorkstationStatus;
import com.improel.samp.entity.Workstation;

public record WorkstationResponseDTO(
        Long id,
        String name,
        WorkstationStatus status

) {
    // Converte a Entidade JPA Workstation para o DTO de resposta.
    public static WorkstationResponseDTO fromEntity(Workstation workstation) {
        return new WorkstationResponseDTO(
                workstation.getId(),
                workstation.getName(),
                workstation.getStatus()
        );
    }
}

// DTO de resposta para trafegar dados das bancadas de montagem sem expor a Entidade JPA direta.

package com.improel.samp.dto;

import com.improel.samp.domain.WorkstationStatus;
import com.improel.samp.entity.Workstation;

public record WorkstationDTO(
        Long id,
        String name,
        WorkstationStatus status

) {
    // Converte a Entidade JPA Workstation para o DTO de resposta.
    public static WorkstationDTO fromEntity(Workstation workstation) {
        return new WorkstationDTO(
                workstation.getId(),
                workstation.getName(),
                workstation.getStatus()
        );
    }
}

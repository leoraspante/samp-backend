// DTO responsável pelas ações realizadas na bancada que exigem apenas a identificação da mesma (RESUME/COMPLETE).

package com.improel.samp.dto.request;

public record WorkstationActionRequestDTO(
        Long workstationId
) {
}

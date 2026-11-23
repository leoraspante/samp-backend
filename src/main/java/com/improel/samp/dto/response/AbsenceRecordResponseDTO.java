// DTO de saída para trafegar registros de ausência sem expor a entidade JPA nem dados sensíveis do User.

package com.improel.samp.dto.response;

import com.improel.samp.domain.AbsenceReason;
import com.improel.samp.entity.AbsenceRecord;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record AbsenceRecordResponseDTO(
        Long id,
        Long userId,
        String userName,
        AbsenceReason reason,
        LocalDate startDate,
        LocalDate endDate,
        String observation,
        LocalDateTime createdAt
) {
    public static AbsenceRecordResponseDTO fromEntity(AbsenceRecord record) {
        return new AbsenceRecordResponseDTO(
                record.getId(),
                record.getUser() != null ? record.getUser().getId() : null,
                record.getUser() != null ? record.getUser().getFullName() : null,
                record.getReason(),
                record.getStartDate(),
                record.getEndDate(),
                record.getObservation(),
                record.getCreatedAt()
        );
    }
}

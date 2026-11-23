package com.improel.samp.dto.response;

import com.improel.samp.entity.ShiftRecord;

import java.time.LocalDateTime;

public record ShiftRecordResponseDTO(

        Long id,
        Long operatorId,
        String operatorName,
        Long workstationId,
        String workstationName,
        LocalDateTime loginTime,
        LocalDateTime logoutTime,
        Boolean hasAnomaly,
        String anomalyNotes
) {

    public static ShiftRecordResponseDTO fromEntity(ShiftRecord shift){
        return new ShiftRecordResponseDTO(
                shift.getId(),
                shift.getOperator() != null ? shift.getOperator().getId() : null,
                shift.getOperator() != null ? shift.getOperator().getFullName() : null,
                shift.getWorkstation() != null ? shift.getWorkstation().getId() : null,
                shift.getWorkstation() != null ? shift.getWorkstation().getName() : null,
                shift.getLoginTime(),
                shift.getLogoutTime(),
                shift.getHasAnomaly(),
                shift.getAnomalyNotes()
        );
    }
}

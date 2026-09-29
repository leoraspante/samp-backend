// Enum contemplando os motivos de ausência do operador.

package com.improel.samp.domain;

public enum AbsenceReason {

    MEDICAL_LEAVE("Atestado Médico"),
    UNEXCUSED("Falta Não Justificada"),
    VACATION("Férias"),
    JUSTIFIED("Falta Justificada");

    private final String description;

    AbsenceReason(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

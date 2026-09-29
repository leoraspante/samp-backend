// Enum contemplando os motivos de parada de produção.

package com.improel.samp.domain;

public enum PauseReason {
    TECHNICAL_DOUBT("Dúvida Técnica"),
    MATERIAL_SHORTAGE("Falta de Material"),
    MAINTENANCE("Manutenção da Bancada / Ferramenta");

    private final String description;

    PauseReason(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

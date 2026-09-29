// Enum contemplando os possíveis estados de uma bancada de montagem.

package com.improel.samp.domain;

public enum WorkstationStatus {

    ACTIVE("Ativa"),
    INACTIVE("Inativa"),
    MAINTENANCE("Em Manutenção");

    private final String description;

    WorkstationStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

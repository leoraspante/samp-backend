// Enum contemplando os diferente estados de produção.

package com.improel.samp.domain;

public enum OrderStatus {
    PENDING("Pendente"),
    IN_PROGRESS("Em Andamento"),
    PAUSED("Pausada"),
    COMPLETED("Concluída"),
    CANCELLED("Cancelada");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

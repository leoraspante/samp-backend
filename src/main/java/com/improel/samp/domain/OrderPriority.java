// Enum contemplando os diferentes níveis de prioridade de um determinado serviço.

package com.improel.samp.domain;

public enum OrderPriority {
    LOW("Baixa"),
    MEDIUM("Média"),
    HIGH("Alta"),
    URGENT("Urgente");

    private final String description;

    OrderPriority(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

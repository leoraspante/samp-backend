// Enum contemplando os estados de produção.

package com.improel.samp.domain;

public enum LogType {
    START("Início de Produção"),
    PAUSE("Pausa de Produção"),
    RESUME("Retomada de Produção"),
    STOP("Parada de Produção"),
    HANDOVER("Troca de Colaborador");

    private final String description;

    LogType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

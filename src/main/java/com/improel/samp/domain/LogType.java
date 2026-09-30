// Enum contemplando os estados de produção.

package com.improel.samp.domain;

public enum LogType {
    START("Início de Produção"),
    PAUSE("Pausa de Produção"),
    RESUME("Retomada de Produção"),
    STOP("Parada de Produção"),
    FINISH_BATCH("Conclusão do Lote"),
    HANDOVER_RELEASE("Passagem de Turno/Bancada"),
    HANDOVER_TAKE("Assunção de Turno/Bancada");

    private final String description;

    LogType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

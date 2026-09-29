// Enum contemplando os perfis de usuário do sistema.

package com.improel.samp.domain;

public enum Role {
    ADMIN("Administrador"),
    MANAGER("Gestor / Supervisor"),
    OPERATOR("Operador");

    private final String description;

    Role (String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

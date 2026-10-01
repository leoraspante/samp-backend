// DTO (Data Transfer Object) para trafegar dados de usuários de forma segura.

package com.improel.samp.dto;

import com.improel.samp.domain.Role;
import com.improel.samp.entity.User;

public record UserDTO(
        Long id,
        String name,
        String registrationNumber,
        String email,
        Role role,
        Boolean active
) {
    // Utilitário estático que converte a Entidade JPA (User) para esta representação mais leve e segura (UserDTO), omitindo dados sensíveis como PIN ou Senha.
    public static UserDTO fromEntity(User user) {
        return new UserDTO(
                user.getId(),
                user.getFullName(),
                user.getRegistrationNumber(),
                user.getEmail(),
                user.getRole(),
                user.getActive()
        );
    }
}

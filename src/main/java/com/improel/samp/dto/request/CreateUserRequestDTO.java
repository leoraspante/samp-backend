package com.improel.samp.dto.request;

import com.improel.samp.domain.Role;
import jakarta.validation.constraints.*;

public record CreateUserRequestDTO(

        @NotBlank(message = "O nome deve ser preenchido.")
        String fullName,

        @NotBlank(message = "O número de registro deve ser informado.")
        String registrationNumber,

        @Email(message = "Formato de e-mail inválido.")
        String email,

        @NotBlank(message = "O Pin deve ser informado.")
        String pinCode,

        Role role
) {
}

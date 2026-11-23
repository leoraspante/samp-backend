// DTO de requisição enviado pelo tablet ao tentar autenticar um operador por PIN

package com.improel.samp.dto.request;

public record PinAuthRequestDTO(
        String pinCode,
        String tabletUuid
) {}

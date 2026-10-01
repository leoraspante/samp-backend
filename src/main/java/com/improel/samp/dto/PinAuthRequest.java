// DTO de requisição enviado pelo tablet ao tentar autenticar um operador por PIN

package com.improel.samp.dto;

public record PinAuthRequest(
        String pinCode,
        String tabletUuid
) {}

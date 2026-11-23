// DTO para dados de produtos/placas alinhado à entidade Product.

package com.improel.samp.dto.response;

import com.improel.samp.entity.Product;

public record ProductResponseDTO(
        Long id,
        String code,
        String name,
        String description,
        Integer targetCycleTimeSeconds,
        Boolean active
) {
    // Converte a entidade JPA Product para o DTO de resposta.
    public static ProductResponseDTO fromEntity(Product product) {
        return new ProductResponseDTO(
                product.getId(),
                product.getCode(),
                product.getName(),
                product.getDescription(),
                product.getTargetCycleTimeSeconds(),
                product.getActive()
        );
    }
}

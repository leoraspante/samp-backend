// DTO para dados de produtos/placas alinhado à entidade Product.

package com.improel.samp.dto;

import com.improel.samp.entity.Product;

public record ProductDTO(
        Long id,
        String code,
        String name,
        String description,
        Integer targetCycleTimeSeconds,
        Boolean active
) {
    // Converte a entidade JPA Product para o DTO de resposta.
    public static ProductDTO fromEntity(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getCode(),
                product.getName(),
                product.getDescription(),
                product.getTargetCycleTimeSeconds(),
                product.getActive()
        );
    }
}

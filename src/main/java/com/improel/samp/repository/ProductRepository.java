// Interface de acesso a dados para a entidade Product.

package com.improel.samp.repository;

import com.improel.samp.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Busca produto pelo código único de SKU.
    Optional<Product> findBySku(String sku);

    // Busca produtos cujo nome contenha o termo pesquisado (Insensível a maiúsculas/minúsculas).
    List<Product> findByNameContainingIgnoreCase(String name);

    // Verifica se já existe o produto cadastrado com o SKU informado.
    boolean existsBySku(String sku);
}

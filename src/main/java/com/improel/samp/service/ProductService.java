// Serviço responsável pelo cadastro e consulta de produtos e tempo-alvo de montagem.

package com.improel.samp.service;

import com.improel.samp.dto.request.CreateProductRequestDTO;
import com.improel.samp.entity.Product;
import com.improel.samp.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // Lista todos os produtos cadastrados.
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    // Busca um produto pelo ID.
    @Transactional(readOnly = true)
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    // Busca um produto pelo seu código interno.
    @Transactional(readOnly = true)
    public Optional<Product> findByCode(String code) {
        return productRepository.findByCode(code);
    }

    // Salva ou atualiza um produto garantindo que não haja duplicidade de código.
    @Transactional
    public Product create(CreateProductRequestDTO request) {
        if (productRepository.existsByCode(request.code())) {
            throw new IllegalArgumentException("Já existe um produto cadastrado com este código");
        }
        Product product = new Product(
                null,
                request.code(),
                request.name(),
                request.description(),
                request.targetCycleTimeSeconds()
        );
        return productRepository.save(product);
    }
}

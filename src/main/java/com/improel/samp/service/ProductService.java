// Serviço responsável pelo cadastro e consulta de produtos e tempo-alvo de montagem.

package com.improel.samp.service;

import com.improel.samp.entity.Product;
import com.improel.samp.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

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
    public Product save(Product product) {
        if (product.getId() == null && product.getCode() != null && productRepository.existsByCode(product.getCode())){
            throw new IllegalArgumentException("Já existe um produto cadastrado com este código");
        }
        return productRepository.save(product);
    }
}

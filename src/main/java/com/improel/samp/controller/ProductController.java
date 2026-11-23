// Controller responsável pela gerência dos produtos / placas.

package com.improel.samp.controller;

import com.improel.samp.dto.request.CreateProductRequestDTO;
import com.improel.samp.dto.response.ProductResponseDTO;
import com.improel.samp.entity.Product;
import com.improel.samp.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // 1. Lista todos os produtos/placas.
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> findAll() {
        List<ProductResponseDTO> products = productService.findAll().stream().map(ProductResponseDTO::fromEntity).toList();
        return ResponseEntity.ok(products);
    }

    // 2. Busca um determinado produto pelo seu ID.
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> findById(@PathVariable Long id) {
        return productService.findById(id)
                .map(ProductResponseDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. Cadastra um novo produto/placa (Painel do Gestor).
    @PostMapping
    public ResponseEntity<ProductResponseDTO> create(@Valid @RequestBody CreateProductRequestDTO request) {
        Product saveProduct = productService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductResponseDTO.fromEntity(saveProduct));
    }
}

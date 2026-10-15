// Controller responsável pela gerência dos produtos / placas.

package com.improel.samp.controller;

import com.improel.samp.dto.ProductDTO;
import com.improel.samp.entity.Product;
import com.improel.samp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 1. Lista todos os produtos/placas.
    @GetMapping
    public ResponseEntity<List<ProductDTO>> findAll() {
        List<ProductDTO> products = productService.findAll().stream().map(ProductDTO::fromEntity).toList();
        return ResponseEntity.ok(products);
    }

    // 2. Busca um determinado produto pelo seu ID.
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
        return productService.findById(id)
                .map(ProductDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. Cadastra um novo produto/placa (Painel do Gestor).
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Product product) {
        try {
            Product saveProduct = productService.save(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(ProductDTO.fromEntity(saveProduct));
        }  catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

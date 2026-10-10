// Controller responsável pela gerência da Ordem de Produção.

package com.improel.samp.controller;

import com.improel.samp.entity.ProductionOrder;
import com.improel.samp.service.ProductionOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class ProductionOrderController {

    private final ProductionOrderService productionOrderService;

    @Autowired
    public ProductionOrderController(ProductionOrderService productionOrderService) {
        this.productionOrderService = productionOrderService;
    }

    // 1. Lista todas as Ordens de Produção.
    @GetMapping
    public ResponseEntity<List<ProductionOrder>> findAll() {
        return ResponseEntity.ok(productionOrderService.findAll());
    }

    // 2. Busca uma Ordem de Produção pelo seu ID.
    @GetMapping("/{id}")
    public ResponseEntity<ProductionOrder> findById(@PathVariable Long id) {
        return productionOrderService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. Cria uma nova Ordem de Produção (Painel do Gestor).
    @PostMapping
    public ResponseEntity<?> create(@RequestBody ProductionOrder order) {
        try {
            ProductionOrder savedOrder = productionOrderService.save(order);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

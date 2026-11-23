// Controller responsável pela gerência da Ordem de Produção.

package com.improel.samp.controller;

import com.improel.samp.dto.request.CreateProductionOrderRequestDTO;
import com.improel.samp.dto.response.ProductionOrderResponseDTO;
import com.improel.samp.entity.ProductionOrder;
import com.improel.samp.service.ProductionOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class ProductionOrderController {

    private final ProductionOrderService productionOrderService;

    // 1. Lista todas as Ordens de Produção.
    @GetMapping
    public ResponseEntity<List<ProductionOrderResponseDTO>> findAll() {
        List<ProductionOrderResponseDTO> orders = productionOrderService.findAll().stream().map(ProductionOrderResponseDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(orders);
    }

    // 2. Busca uma Ordem de Produção pelo seu ID.
    @GetMapping("/{id}")
    public ResponseEntity<ProductionOrderResponseDTO> findById(@PathVariable Long id) {
        return productionOrderService.findById(id)
                .map(ProductionOrderResponseDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. Cria uma nova Ordem de Produção (Painel do Gestor).
    @PostMapping
    public ResponseEntity<ProductionOrderResponseDTO> create(@Valid @RequestBody CreateProductionOrderRequestDTO request) {
        ProductionOrder savedOrder = productionOrderService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductionOrderResponseDTO.fromEntity(savedOrder));
    }
}

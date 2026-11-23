// Serviço responsável pelo ciclo de vida e acompanhamento das Ordens de Produção

package com.improel.samp.service;

import com.improel.samp.domain.OrderStatus;
import com.improel.samp.dto.request.CreateProductionOrderRequestDTO;
import com.improel.samp.entity.Product;
import com.improel.samp.entity.ProductionOrder;
import com.improel.samp.repository.ProductRepository;
import com.improel.samp.repository.ProductionOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductionOrderService {

    private final ProductionOrderRepository productionOrderRepository;
    private final ProductRepository productRepository;

    // Lista todas as Ordens de Produção.
    @Transactional(readOnly = true)
    public List<ProductionOrder> findAll() {
        return productionOrderRepository.findAll();
    }

    // Busca uma OP pelo seu ID.
    @Transactional(readOnly = true)
    public Optional<ProductionOrder> findById(Long id) {
        return productionOrderRepository.findById(id);
    }

    // Busca uma OP pelo seu número identificador (orderNumber).
    @Transactional(readOnly = true)
    public Optional<ProductionOrder> findByOrderNumber(String orderNumber) {
        return productionOrderRepository.findByOrderNumber(orderNumber);
    }

    // Lista todas as OPs com base em seu status (Ex: PENDING, IN_PROGRESS,
    // COMPLETED).
    @Transactional(readOnly = true)
    public List<ProductionOrder> findByStatus(OrderStatus status) {
        return productionOrderRepository.findByStatus(status);
    }

    // Cria uma nova Ordem de Produção a partir do DTO, validando duplicidade de número e existência do produto.
    @Transactional
    public ProductionOrder create(CreateProductionOrderRequestDTO request) {
        if (productionOrderRepository.existsByOrderNumber(request.orderNumber())) {
            throw new IllegalArgumentException("Já existe uma Ordem de Produção cadastrada com este número.");
        }

        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Produto não encontrado para o ID informado: " + request.productId()));

        ProductionOrder order = new ProductionOrder(
                null,
                request.orderNumber(),
                product,
                request.targetQuantity(),
                OrderStatus.PENDING,
                request.priority(),
                request.dueDate(),
                request.notes()
        );

        return productionOrderRepository.save(order);
    }

    // Atualiza o status de uma Ordem de Produção.
    @Transactional
    public ProductionOrder updateStatus(Long id, OrderStatus newStatus) {
        ProductionOrder order = productionOrderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Ordem de Produção não encontrada para o ID informado: " + id));
        order.setStatus(newStatus);
        return productionOrderRepository.save(order);
    }
}

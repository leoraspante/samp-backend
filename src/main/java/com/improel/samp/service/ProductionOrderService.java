// Serviço responsável pelo ciclo de vida e acompanhamento das Ordens de Produção

package com.improel.samp.service;

import com.improel.samp.domain.OrderStatus;
import com.improel.samp.entity.ProductionOrder;
import com.improel.samp.repository.ProductionOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductionOrderService {

    private final ProductionOrderRepository productionOrderRepository;

    @Autowired
    public ProductionOrderService(ProductionOrderRepository productionOrderRepository) {
        this.productionOrderRepository = productionOrderRepository;
    }

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

    // Lista todas as OPs com base em seu status (Ex: PENDING, IN_PROGRESS, COMPLETED).
    @Transactional(readOnly = true)
    public List<ProductionOrder> findByStatus(OrderStatus status)  {
        return productionOrderRepository.findByStatus(status);
    }

    // Cria ou atualiza uma Ordem de Produção, validando duplicidade de número.
    @Transactional
    public ProductionOrder save(ProductionOrder order) {
        if (order.getId() == null && order.getOrderNumber() != null && productionOrderRepository.existsByOrderNumber(order.getOrderNumber())) {
            throw new IllegalArgumentException("Já existe uma Ordem de Produção cadastrada com este número");
        }
        return productionOrderRepository.save(order);
    }

    // Atualiza o status de uma Ordem de Produção.
    @Transactional
    public ProductionOrder updateStatus(Long id, OrderStatus newStatus) {
        ProductionOrder order = productionOrderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ordem de Produção não encontrada para o ID informado: " + id));
        order.setStatus(newStatus);
        return productionOrderRepository.save(order);
    }
}

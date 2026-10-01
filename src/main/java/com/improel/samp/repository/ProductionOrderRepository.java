// Interface de acesso a dados para a entidade ProductionOrder.

package com.improel.samp.repository;

import com.improel.samp.domain.OrderPriority;
import com.improel.samp.domain.OrderStatus;
import com.improel.samp.entity.ProductionOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductionOrderRepository extends JpaRepository<ProductionOrder, Long> {

    // Busca a Ordem de Produção pelo seu código único (Ex: "OP-2026-0042").
    Optional<ProductionOrder> findByOrderNumber(String orderNumber);

    // Lista todas as OPs filtradas por determinado status (Ex: PENDING, IN_PROGRESS).
    List<ProductionOrder> findByStatus(OrderStatus status);

    // Lista OPs filtradas por prioridade (Ex: HIGH, URGENT).
    List<ProductionOrder> findByPriority(OrderPriority priority);

    // Lista OPs filtradas por status e prioridade simultaneamente.
    List<ProductionOrder> findByStatusAndPriority(OrderStatus status, OrderPriority priority);

    // Verifica se já existe uma OP cadastrada com o número informado.
    boolean existsByOrderNumber(String orderNumber);
}

// Classe representando a ordem de produção emitida pela gestão.

package com.improel.samp.entity;

import com.improel.samp.domain.OrderPriority;
import com.improel.samp.domain.OrderStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "tb_production_order")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class ProductionOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    // Atributos.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Código identificador único da Ordem de Produção (Ex: "OP-2026-0042").
    @Column(name = "order_number", nullable = false, unique = true, length = 50)
    private String orderNumber;

    // Relacionamento Muitos-para-Um: Diversas OP podem estar atreladas ao mesmo
    // Produto.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "target_quantity", nullable = false)
    private Integer targetQuantity;

    @Column(name = "produced_quantity", nullable = false)
    private Integer producedQuantity = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private OrderStatus status = OrderStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false, length = 30)
    private OrderPriority priority = OrderPriority.MEDIUM;

    // Tempo líquido total acumulado na montagem do lote em segundos (Campo
    // reservado para futuras integrações).
    @Column(name = "total_assembly_seconds")
    private Long totalAssemblySeconds = 0L;

    // Observações ou instruções técnicas enviadas pelo gestor a equipe de montagem.
    @Column(name = "notes", length = 500)
    private String notes;

    // Data/Hora de liberação para início real na montagem.
    @Column(name = "start_time")
    private LocalDateTime startTime;

    // Data/Hora do encerramento real da montagem (Lote finalizado).
    @Column(name = "end_time")
    private LocalDateTime endTime;

    // Prazo limite para entrega do lote (Combinado com o cliente).
    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Setter(AccessLevel.NONE)
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Construtor modificado.
    public ProductionOrder(Long id, String orderNumber, Product product, Integer targetQuantity, OrderStatus status,
            OrderPriority priority, LocalDateTime dueDate, String notes) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.product = product;
        this.targetQuantity = targetQuantity;
        this.producedQuantity = 0;
        this.status = (status != null) ? status : OrderStatus.PENDING;
        this.priority = (priority != null) ? priority : OrderPriority.MEDIUM;
        this.totalAssemblySeconds = 0L;
        this.dueDate = dueDate;
        this.notes = notes;
    }

    // Callback JPA para preencher a data de criação antes de salvar no banco de
    // dados.
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // toString personalizado para logs e depuração.
    @Override
    public String toString() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        String formattedStart = (startTime != null) ? startTime.format(dateTimeFormatter) : "Não iniciada";
        String formattedEnd = (endTime != null) ? endTime.format(dateTimeFormatter) : "Não finalizada";
        String formattedDue = (dueDate != null) ? dueDate.format(dateTimeFormatter) : "Sem prazo definido";

        return "ProductionOrder {" +
                "ID=" + id +
                ", Número OP='" + orderNumber + '\'' +
                ", Produto=" + (product != null ? product.getName() : "N/A") +
                ", Meta=" + targetQuantity +
                ", Produzido=" + producedQuantity +
                ", Status=" + status +
                ", Prioridade=" + priority +
                ", Início=" + formattedStart +
                ", Fim=" + formattedEnd +
                ", Prazo Limite=" + formattedDue +
                '}';
    }
}

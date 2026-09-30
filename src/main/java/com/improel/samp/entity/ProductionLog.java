// Classe representando a linha do tempo de produção.

package com.improel.samp.entity;

import com.improel.samp.domain.LogType;
import com.improel.samp.domain.PauseReason;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Entity
@Table(name = "tb_production_log")      // Nome da tabela no banco de dados.
public class ProductionLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    // Relacionamento Muitos-para-Um: A qual Ordem de Produção este log pertence.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "production_order_id", nullable = false)
    private ProductionOrder productionOrder;

    // Relacionamento Muitos-para-Um: Em qual bancada física ocorreu o evento.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workstation_id", nullable = false)
    private Workstation workstation;

    // Relacionamento Muitos-para-Um: Operador responsável pelo registro.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operator_id",  nullable = false)
    private User operator;

    // Relacionamento Muitos-para-Um: Operador com quem foi feita a troca (Handover).
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transferred_from_user_id")
    private User transferredFromUser;

    @Enumerated(EnumType.STRING)
    @Column(name = "log_type", nullable = false, length = 30)
    private LogType logType;

    @Enumerated(EnumType.STRING)
    @Column(name = "pause_reason", length = 50)
    private PauseReason pauseReason;

    // Instante de início exato da sessão/marcação.
    @Column(name = "start_timestamp", nullable = false)
    private LocalDateTime startTimestamp;

    // Instante de término da sessão/marcação.
    @Column(name = "end_timestamp")
    private LocalDateTime endTimestamp;

    // Tempo líquido da sessão decorrido em segundos.
    @Column(name = "duration_seconds")
    private Long durationSeconds = 0L;

    // Quantidade de peças contabilizadas/apontadas nesta sessão (ex: parcial informada pelo gestor no Handover ou total na Conclusão).
    @Column(name = "completed_quantity")
    private Integer completedQuantity = 0;

    // Justificativas ou observações, gravas exclusivamente pelo Gestor.
    @Column(name = "remarks", length = 500)
    private String remarks;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Construtores.
    public ProductionLog() {}

    public ProductionLog(Long id, ProductionOrder productionOrder, Workstation workstation, User operator, LogType logType, LocalDateTime startTimestamp) {
        this.id = id;
        this.productionOrder = productionOrder;
        this.workstation = workstation;
        this.operator = operator;
        this.logType = logType;
        this.startTimestamp = (startTimestamp != null) ? startTimestamp : LocalDateTime.now();
    }

    // Callback JPA para preencher a data de criação antes de salvar no banco de dados.
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.startTimestamp == null) {
            this.startTimestamp = LocalDateTime.now();
        }
    }

    // Getters e Setters.
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductionOrder getProductionOrder() {
        return productionOrder;
    }

    public void setProductionOrder(ProductionOrder productionOrder) {
        this.productionOrder = productionOrder;
    }

    public Workstation getWorkstation() {
        return workstation;
    }

    public void setWorkstation(Workstation workstation) {
        this.workstation = workstation;
    }

    public User getOperator() {
        return operator;
    }

    public void setOperator(User operator) {
        this.operator = operator;
    }

    public User getTransferredFromUser() {
        return transferredFromUser;
    }

    public void setTransferredFromUser(User transferredFromUser) {
        this.transferredFromUser = transferredFromUser;
    }

    public LogType getLogType() {
        return logType;
    }

    public void setLogType(LogType logType) {
        this.logType = logType;
    }

    public PauseReason getPauseReason() {
        return pauseReason;
    }

    public void setPauseReason(PauseReason pauseReason) {
        this.pauseReason = pauseReason;
    }

    public LocalDateTime getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(LocalDateTime startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public LocalDateTime getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(LocalDateTime endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public Long getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(Long durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public Integer getCompletedQuantity() {
        return completedQuantity;
    }

    public void setCompletedQuantity(Integer completedQuantity) {
        this.completedQuantity = completedQuantity;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Equals() e HashCode().
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductionLog that = (ProductionLog) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // toString personalizado para logs e depuração.
    @Override
    public String toString() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedStart = (startTimestamp != null) ? startTimestamp.format(dateTimeFormatter) : "N/A";
        String formattedEnd = (endTimestamp != null) ? endTimestamp.format(dateTimeFormatter) : "Em andamento";

        return "ProductionLog {" +
                "ID=" + id +
                ", OP=" + (productionOrder != null ? productionOrder.getOrderNumber() : "N/A") +
                ", Operador=" + (operator != null ? operator.getFullName() : "N/A") +
                ", Bancada=" + (workstation != null ? workstation.getId() : "N/A") +
                ", Tipo=" + logType +
                ", Início=" + formattedStart +
                ", Fim=" + formattedEnd +
                '}';
    }
}

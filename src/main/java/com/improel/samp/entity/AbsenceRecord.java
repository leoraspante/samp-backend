// Classe representando o registro de ausência/falta do colaborador.

package com.improel.samp.entity;

import com.improel.samp.domain.AbsenceReason;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Entity
@Table(name = "tb_absence_record")      // Nome da tabela no banco de dados.
public class AbsenceRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    // Atributos.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relacionamento Muitos-para-Um: Diversos registros de ausência pertencem a um Usuário/Colaborador.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "reason", nullable = false)
    private AbsenceReason reason;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "observation", length = 255)
    private String observation;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Construtores.
    public AbsenceRecord() {}

    public AbsenceRecord(Long id, User user, AbsenceReason reason, LocalDate startDate, LocalDate endDate, String observation) {
        this.id = id;
        this.user = user;
        this.reason = reason;
        this.startDate = startDate;
        this.endDate = endDate;
        this.observation = observation;
    }

    // Callback JPA para preencher a data de criação antes de salvar no banco de dados.
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters e Setters.
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AbsenceReason getReason() {
        return reason;
    }

    public void setReason(AbsenceReason reason) {
        this.reason = reason;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Equals() e HashCode().
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbsenceRecord that = (AbsenceRecord) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // toString personalizado para logs e depuração.
    @Override
    public String toString() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        String formattedStart = (startDate != null) ? startDate.format(dateFormatter) : "N/A";
        String formattedEnd = (endDate != null) ? endDate.format(dateFormatter) : "N/A";
        String formattedCreatedAt = (createdAt != null) ? createdAt.format(dateTimeFormatter) : "N/A";

        return "AbsenceRecord {" +
                "ID=" + id +
                ", Colaborador=" + (user != null ? user.getFullName() : "N/A") +
                ", Motivo=" + (reason != null ? reason.getDescription() : "N/A") +
                ", Início=" + formattedStart +
                ", Fim=" + formattedEnd +
                ", Observação='" + observation + '\'' +
                ", Criado em=" + formattedCreatedAt +
                '}';
    }

}

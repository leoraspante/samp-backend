// Classe representando a estação de trabalho.

package com.improel.samp.entity;

import com.improel.samp.domain.WorkstationStatus;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Entity
@Table(name = "tb_workstation")     // Nome da tabela no banco de dados.
public class Workstation implements Serializable {

    private static final long serialVersionUID = 1L;

    // Atributos.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private WorkstationStatus status = WorkstationStatus.ACTIVE;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt ;

    // Construtores.
    public Workstation() {}

    public Workstation(Long id, String name, WorkstationStatus status) {
        this.id = id;
        this.name = name;
        this.status = (status != null) ? status : WorkstationStatus.ACTIVE;
    }

    // Callback JPA para preencher a data de criação antes de salvar no banco.
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters e Setters.
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WorkstationStatus getStatus() {
        return status;
    }

    public void setStatus(WorkstationStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Equals() e HashCode().
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Workstation that = (Workstation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // toString personalizado para logs e depuração.
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedCreatedAt = (createdAt != null) ? createdAt.format(formatter) : "N/A";

        return "Workstation {" +
                "ID=" + id +
                ", Bancada='" + name + '\'' +
                ", Estado=" + (status != null ? status.getDescription() : "N/A") +
                ", Criado em=" + formattedCreatedAt +
                '}';
    }
}

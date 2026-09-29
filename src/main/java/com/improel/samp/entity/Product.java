// Classe representando os produtos/placas gerenciadas pelo sistema e destinadas a montagem.

package com.improel.samp.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Entity
@Table(name = "tb_product")     // Nome da tabela no banco de dados.
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    // Atributos.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, unique = true, length = 50)
    private  String code;

    @Column(name = "name", nullable = false, length = 100)
    private  String name;

    @Column(name = "description", length = 255)
    private  String description;

    @Column(name = "target_cycle_time_seconds")
    private Integer targetCycleTimeSeconds;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Construtores.
    public Product() {}

    public Product(Long id, String code, String name, String description, Integer targetCycleTimeSeconds) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
        this.targetCycleTimeSeconds = targetCycleTimeSeconds;
        this.active = true;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTargetCycleTimeSeconds() {
        return targetCycleTimeSeconds;
    }

    public void setTargetCycleTimeSeconds(Integer targetCycleTimeSeconds) {
        this.targetCycleTimeSeconds = targetCycleTimeSeconds;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Equals() e HashCode().
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // toString personalizado para logs e depuração.
    @Override
    public String toString() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String formattedCreatedAt = (createdAt != null) ? createdAt.format(dateTimeFormatter) : "N/A";

        return "Product {" +
                "ID=" + id +
                ", Código='" + code + '\'' +
                ", Nome='" + name + '\'' +
                ", Tempo Alvo (s)=" + (targetCycleTimeSeconds != null ? targetCycleTimeSeconds : "Não informado") +
                ", Ativo=" + active +
                ", Criado em=" + formattedCreatedAt +
                '}';
    }
}

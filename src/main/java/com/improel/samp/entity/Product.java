// Classe representando os produtos/placas gerenciadas pelo sistema e destinadas a montagem.

package com.improel.samp.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_product")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    // Atributos.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "target_cycle_time_seconds")
    private Integer targetCycleTimeSeconds;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @Setter(AccessLevel.NONE)
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Construtor customizado.
    public Product(Long id, String code, String name, String description, Integer targetCycleTimeSeconds) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
        this.targetCycleTimeSeconds = targetCycleTimeSeconds;
        this.active = true;
    }

    // Callback JPA para preencher a data de criação antes de salvar no banco de
    // dados.
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Relacionamento Um-para-Muitos contemplando as fotos com instruções de
    // montagem.
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> images = new ArrayList<>();

    // Métodos utilitários para auxiliar a gestão de fotos.
    public void addImage(ProductImage image) {
        images.add(image);
        image.setProduct(this);
    }

    public void removeImage(ProductImage image) {
        images.remove(image);
        image.setProduct(null);
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

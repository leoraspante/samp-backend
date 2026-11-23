// Classe representando as imagens de intrução de montagem do produto.

package com.improel.samp.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_product_image")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class ProductImage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "image_url", nullable = false, length = 255)
    private String imageUrl;

    // Sequência de fotos para ordem de montagem (Ex: Passo_1 / Passo_2 etc...).
    @Column(name = "step_order", nullable = false)
    private Integer stepOrder;

    // Descrição da montagem (Ex: Instalação dos resistores, diodos etc...).
    @Column(name = "step_description", nullable = false, length = 255)
    private String stepDescription;

    @Setter(AccessLevel.NONE)
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Construtor customizado.
    public ProductImage(Long id, Product product, String imageUrl, Integer stepOrder, String stepDescription) {
        this.id = id;
        this.product = product;
        this.imageUrl = imageUrl;
        this.stepOrder = stepOrder;
        this.stepDescription = stepDescription;
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
        return "ProductImage {" +
                "ID=" + id +
                ", Passo=" + stepOrder +
                ", Descrição='" + stepDescription + '\'' +
                ", URL='" + imageUrl + '\'' +
                '}';
    }
}

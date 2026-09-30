// Classe representando as imagens de intrução de montagem do produto.

package com.improel.samp.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "tb_product_image")
public class ProductImage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "image_url",  nullable = false, length = 255)
    private String imageUrl;

    // Sequência de fotos para ordem de montagem (Ex: Passo_1 / Passo_2 etc...).
    @Column(name = "step_order", nullable = false)
    private Integer stepOrder;

    // Descrição da montagem (Ex: Instalação dos resistores, diodos etc...).
    @Column(name = "step_description",  nullable = false, length = 255)
    private String stepDescription;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Construtores.
    public ProductImage() {}

    public ProductImage(Long id, Product product, String imageUrl, Integer stepOrder, String stepDescription) {
        this.id = id;
        this.product = product;
        this.imageUrl = imageUrl;
        this.stepOrder = stepOrder;
        this.stepDescription = stepDescription;
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getStepOrder() {
        return stepOrder;
    }

    public void setStepOrder(Integer stepOrder) {
        this.stepOrder = stepOrder;
    }

    public String getStepDescription() {
        return stepDescription;
    }

    public void setStepDescription(String stepDescription) {
        this.stepDescription = stepDescription;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Equals() e HashCode().
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductImage that = (ProductImage) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
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

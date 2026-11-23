// Classe representando a estação de trabalho.

package com.improel.samp.entity;

import com.improel.samp.domain.WorkstationStatus;
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
@Table(name = "tb_workstation")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
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

    // Vínculo lógico com o dispositivo físico da bancada (Tablet).
    @Column(name = "tablet_uuid", unique = true, length = 100)
    private String tabletUuid;

    @Setter(AccessLevel.NONE)
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Construtor customizado.
    public Workstation(Long id, String name, WorkstationStatus status, String tabletUuid) {
        this.id = id;
        this.name = name;
        this.status = (status != null) ? status : WorkstationStatus.ACTIVE;
        this.tabletUuid = tabletUuid;
    }

    // Callback JPA para preencher a data de criação antes de salvar no banco.
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // toString personalizado para logs e depuração.
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedCreatedAt = (createdAt != null) ? createdAt.format(formatter) : "N/A";
        String formattedUuid = (tabletUuid != null && !tabletUuid.isBlank()) ? tabletUuid : "Não vinculado";

        return "Workstation {" +
                "ID=" + id +
                ", Bancada='" + name + '\'' +
                ", Estado=" + (status != null ? status.getDescription() : "N/A") +
                ", Tablet UUID='" + formattedUuid + '\'' +
                ", Criado em=" + formattedCreatedAt +
                '}';
    }
}

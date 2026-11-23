// Classe representando o registro de ausência/falta do colaborador.

package com.improel.samp.entity;

import com.improel.samp.domain.AbsenceReason;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "tb_absence_record")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class AbsenceRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    // Atributos.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relacionamento Muitos-para-Um: Diversos registros de ausência pertencem a um
    // Usuário/Colaborador.
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

    @Setter(AccessLevel.NONE)
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Construtor modificado.
    public AbsenceRecord(Long id, User user, AbsenceReason reason, LocalDate startDate, LocalDate endDate,
            String observation) {
        this.id = id;
        this.user = user;
        this.reason = reason;
        this.startDate = startDate;
        this.endDate = endDate;
        this.observation = observation;
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

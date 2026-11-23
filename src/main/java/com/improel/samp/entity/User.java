// Classe representando o colaborador/usuário do sistema.

package com.improel.samp.entity;

import com.improel.samp.domain.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "tb_user")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    // Atributos.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(name = "registration_num", nullable = false, unique = true, length = 30)
    private String registrationNumber;

    @Email(message = "Formato de e-mail inválido")
    @Column(name = "email", nullable = true, unique = true, length = 100)
    private String email;

    // Hash criptografado do PIN.
    @Column(name = "pin_code", nullable = false, length = 100)
    private String pinCode;

    @Column(name = "allow_same_pin_reuse", nullable = false)
    private Boolean allowSamePinReuse = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 30)
    private Role role = Role.OPERATOR;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    // Responsável por contabilizar esquecimentos de inicialização/encerramento do
    // sistema.
    @Column(name = "shift_anomaly_count", nullable = false)
    private Integer shiftAnomalyCount = 0;

    @Setter(AccessLevel.NONE)
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Construtor customizado.
    public User(Long id, String fullName, String registrationNumber, String email, String pinCode, Role role) {
        this.id = id;
        this.fullName = fullName;
        this.registrationNumber = registrationNumber;
        this.email = email;
        this.pinCode = pinCode;
        this.role = (role != null) ? role : Role.OPERATOR;
        this.allowSamePinReuse = true;
        this.active = true;
        this.shiftAnomalyCount = 0;
    }

    // Callback JPA para preencher a data de criação antes de salvar no banco de
    // dados.
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Métodos utilitários - Contabilização de não uso do sistema.
    public void incrementShiftAnomalyCount() {
        this.shiftAnomalyCount++;
    }

    public void resetShiftAnomalyCount() {
        this.shiftAnomalyCount = 0;
    }

    // toString personalizado para logs e depuração (PIN oculto por segurança).
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String formattedCreatedAt = (createdAt != null) ? createdAt.format(formatter) : "N/A";

        return "User {" +
                "ID=" + id +
                ", Nome='" + fullName + '\'' +
                ", Matrícula='" + registrationNumber + '\'' +
                ", Email='" + (email != null ? email : "Não informado") + '\'' +
                ", Perfil=" + (role != null ? role.getDescription() : "N/A") +
                ", Ativo=" + active +
                ", Anomalias de Turno=" + shiftAnomalyCount +
                ", Criado em=" + formattedCreatedAt +
                '}';
    }
}

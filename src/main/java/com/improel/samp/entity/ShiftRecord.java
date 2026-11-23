// Classe representando o registro de ponto/jornada do colaborador.

package com.improel.samp.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "tb_shift_record")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class ShiftRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    // Atributos.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operator_id", nullable = false)
    private User operator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workstation_id", nullable = false)
    private Workstation workstation;

    @Column(name = "login_time", nullable = false)
    private LocalDateTime loginTime;

    @Column(name = "logout_time")
    private LocalDateTime logoutTime;

    // Sinaliza se ouve atraso no início da jornada, ou que o expediente não foi
    // devidamente encerrado.
    @Column(name = "has_anomaly", nullable = false)
    private Boolean hasAnomaly = false;

    @Column(name = "anomaly_notes", length = 255)
    private String anomalyNotes;

    // Construtor modificado.
    public ShiftRecord(User operator, Workstation workstation, LocalDateTime loginTime) {
        this.operator = operator;
        this.workstation = workstation;
        this.loginTime = loginTime;
        this.hasAnomaly = false;
    }

    // toString personalizado para logs e depuração.
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedLogin = (loginTime != null) ? loginTime.format(formatter) : "N/A";
        String formattedLogout = (logoutTime != null) ? logoutTime.format(formatter) : "Em andamento";

        return "ShiftRecord {" +
                "ID=" + id +
                ", Operador=" + (operator != null ? operator.getFullName() : "N/A") +
                ", Bancada=" + (workstation != null ? workstation.getName() : "N/A") +
                ", Entrada=" + formattedLogin +
                ", Saída=" + formattedLogout +
                ", Anomalia=" + hasAnomaly +
                '}';
    }
}

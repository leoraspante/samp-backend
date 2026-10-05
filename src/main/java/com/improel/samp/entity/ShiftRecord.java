// Classe representando o registro de ponto/jornada do colaborador.

package com.improel.samp.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Entity
@Table(name = "tb_shift_record")
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

    // Sinaliza se ouve atraso no início da jornada, ou que o expediente não foi devidamente encerrado.
    @Column(name = "has_anomaly", nullable = false)
    private Boolean hasAnomaly = false;

    @Column(name = "anomaly_notes", length = 255)
    private String anomalyNotes;

    // Construtores.
    public ShiftRecord() {}

    public ShiftRecord(User operator, Workstation workstation, LocalDateTime loginTime) {
        this.operator = operator;
        this.workstation = workstation;
        this.loginTime = loginTime;
        this.hasAnomaly = false;
    }

    // Getters e Setters.
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getOperator() {
        return operator;
    }

    public void setOperator(User operator) {
        this.operator = operator;
    }

    public Workstation getWorkstation() {
        return workstation;
    }

    public void setWorkstation(Workstation workstation) {
        this.workstation = workstation;
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }

    public LocalDateTime getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(LocalDateTime logoutTime) {
        this.logoutTime = logoutTime;
    }

    public Boolean getHasAnomaly() {
        return hasAnomaly;
    }

    public void setHasAnomaly(Boolean hasAnomaly) {
        this.hasAnomaly = hasAnomaly;
    }

    public String getAnomalyNotes() {
        return anomalyNotes;
    }

    public void setAnomalyNotes(String anomalyNotes) {
        this.anomalyNotes = anomalyNotes;
    }

    // Equals() e HashCode().
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShiftRecord that = (ShiftRecord) o;
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

// Serviço responsável pelo controle de jornada e tolerâncias de hoário.
package com.improel.samp.service;

import com.improel.samp.entity.ShiftRecord;
import com.improel.samp.entity.User;
import com.improel.samp.entity.Workstation;
import com.improel.samp.repository.ShiftRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@Service
public class ShiftService {

    // Formato padrão para exibição de data/hora (HH:mm:ss).
    private static final java.time.format.DateTimeFormatter TIME_FORMATTER = java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss");

    private final ShiftRecordRepository shiftRecordRepository;

    // Horários oficiais e tolerâncias.
    private static final LocalTime EXPECTED_START =  LocalTime.of(8, 0);
    private static final LocalTime MAX_START_TOLERANCE = LocalTime.of(8, 15);
    private static final LocalTime EXPECTED_END =  LocalTime.of(17, 30);
    private static final LocalTime MAX_END_TOLERANCE = LocalTime.of(18, 0);

    @Autowired
    public ShiftService(ShiftRecordRepository shiftRecordRepository) {
        this.shiftRecordRepository = shiftRecordRepository;
    }

    // Registra a entrada do operador na bancada.
    @Transactional
    public ShiftRecord registerLogin(User operator, Workstation workstation) {
        // Verifica se já existe um turno aberto para evitar duplicidade.
        Optional<ShiftRecord> openShift = shiftRecordRepository.findByOperatorAndLogoutTimeIsNull(operator);
        if (openShift.isPresent()) {
            return openShift.get();
        }

        LocalDateTime now = LocalDateTime.now();
        ShiftRecord shift = new ShiftRecord(operator, workstation, now);

        // Validação de Atraso (Tolerância de 15 minutos).
        if (now.toLocalTime().isAfter(MAX_START_TOLERANCE)) {
            shift.setHasAnomaly(true);
            shift.setAnomalyNotes("Atraso no início da jornada. Entrada registrada às: " + now.toLocalTime().format(TIME_FORMATTER));
            operator.incrementShiftAnomalyCount();
        }

        return shiftRecordRepository.save(shift);
    }

    // Registra a saída/encerramento do turno.
    @Transactional
    public ShiftRecord registerLogout(User operator) {
        ShiftRecord openShift = shiftRecordRepository.findByOperatorAndLogoutTimeIsNull(operator)
                .orElseThrow(() -> new IllegalArgumentException("Nenhum turno aberto encontrado para este operador"));

        LocalDateTime now = LocalDateTime.now();
        openShift.setLogoutTime(now);

        // Validação de saída (Tolerância de 30 minutos após as 17:30)
        if (now.toLocalTime().isAfter(MAX_END_TOLERANCE)) {
            openShift.setHasAnomaly(true);
            String existingNotes = openShift.getAnomalyNotes() != null ? openShift.getAnomalyNotes()+ " | " : "";
            openShift.setAnomalyNotes(existingNotes + "Usuário não encerrou o sistema. Encerramento automático ocorreu às: " + now.toLocalTime().format(TIME_FORMATTER));
            operator.incrementShiftAnomalyCount();
        }
        return shiftRecordRepository.save(openShift);
    }
}

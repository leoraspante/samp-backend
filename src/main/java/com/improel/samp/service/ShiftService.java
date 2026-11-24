// Serviço responsável pelo controle de jornada e tolerâncias de hoário.
package com.improel.samp.service;

import com.improel.samp.config.ShiftProperties;
import com.improel.samp.dto.request.EndShiftRequestDTO;
import com.improel.samp.dto.request.StartShiftRequestDTO;
import com.improel.samp.entity.ShiftRecord;
import com.improel.samp.entity.User;
import com.improel.samp.entity.Workstation;
import com.improel.samp.repository.ShiftRecordRepository;
import com.improel.samp.repository.UserRepository;
import com.improel.samp.repository.WorkstationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShiftService {

    private final ShiftProperties shiftProperties;
    private final WorkstationRepository workstationRepository;

    // Formato padrão para exibição de data/hora (HH:mm:ss).
    private static final java.time.format.DateTimeFormatter TIME_FORMATTER = java.time.format.DateTimeFormatter
            .ofPattern("HH:mm:ss");

    private final ShiftRecordRepository shiftRecordRepository;
    private final UserRepository userRepository;

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
        if (now.toLocalTime().isAfter(shiftProperties.getMaxStartTolerance())) {
            shift.setHasAnomaly(true);
            shift.setAnomalyNotes(
                    "Atraso no início da jornada. Entrada registrada às: " + now.toLocalTime().format(TIME_FORMATTER));
            operator.incrementShiftAnomalyCount();
            userRepository.save(operator);
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
        if (now.toLocalTime().isAfter(shiftProperties.getMaxEndTolerance())) {
            openShift.setHasAnomaly(true);
            String existingNotes = openShift.getAnomalyNotes() != null ? openShift.getAnomalyNotes() + " | " : "";
            openShift.setAnomalyNotes(
                    existingNotes + "Usuário não encerrou o sistema. Encerramento automático ocorreu às: "
                            + now.toLocalTime().format(TIME_FORMATTER));
            operator.incrementShiftAnomalyCount();
            userRepository.save(operator);
        }
        return shiftRecordRepository.save(openShift);
    }

    @Transactional
    public ShiftRecord startShift(StartShiftRequestDTO request) {
        User operator = userRepository.findById(request.operatorId())
                .orElseThrow(() -> new IllegalArgumentException("Operador não encontrado."));

        Workstation workstation = workstationRepository.findById(request.workstationId())
                .orElseThrow(() -> new IllegalArgumentException("Bancada não encontrada."));

        return registerLogin(operator, workstation);
    }

    @Transactional
    public ShiftRecord endShift(EndShiftRequestDTO request) {
        User operator = userRepository.findById(request.operatorId())
                .orElseThrow(() -> new IllegalArgumentException("Operador não encontrado."));

        return registerLogout(operator);
    }

    @Transactional(readOnly = true)
    public Optional<ShiftRecord> findOpenShift(Long operatorId) {
        User operator = userRepository.findById(operatorId)
                .orElseThrow(() -> new IllegalArgumentException("Operador não encontrado."));

        return shiftRecordRepository.findByOperatorAndLogoutTimeIsNull(operator);
    }

    @Transactional(readOnly = true)
    public List<ShiftRecord> findAnomalies() {
        return shiftRecordRepository.findByHasAnomalyTrueOrderByLoginTimeDesc();
    }
}

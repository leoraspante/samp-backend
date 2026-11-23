// Serviço responsável pelo registro e consulta de ausências, faltas e atestados dos colaboradores.

package com.improel.samp.service;

import com.improel.samp.dto.request.CreateAbsenceRecordRequestDTO;
import com.improel.samp.entity.AbsenceRecord;
import com.improel.samp.entity.User;
import com.improel.samp.repository.AbsenceRecordRepository;
import com.improel.samp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AbsenceService {

    private final AbsenceRecordRepository absenceRecordRepository;
    private final UserRepository userRepository;

    // Lista todos os registros de ausência.
    @Transactional(readOnly = true)
    public List<AbsenceRecord> findAll() {
        return absenceRecordRepository.findAll();
    }

    // Lista o histórico de ausências de um operador específico.
    @Transactional(readOnly = true)
    public List<AbsenceRecord> findByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new IllegalArgumentException("Usuário não encontrado para o ID informado: " + userId));
        return absenceRecordRepository.findByUser(user);
    }

    // Registra uma nova ausência/atestado para o colaborador a partir do DTO de entrada.
    @Transactional
    public AbsenceRecord create(CreateAbsenceRecordRequestDTO request) {
        if (request.endDate().isBefore(request.startDate())) {
            throw new IllegalArgumentException("A data final da ausência não pode ser anterior à data inicial.");
        }

        User user = userRepository.findById(request.userId())
                .orElseThrow(
                        () -> new IllegalArgumentException("Usuário não encontrado para o ID informado: " + request.userId()));

        AbsenceRecord absenceRecord = new AbsenceRecord(
                null,
                user,
                request.reason(),
                request.startDate(),
                request.endDate(),
                request.observation()
        );

        return absenceRecordRepository.save(absenceRecord);
    }
}

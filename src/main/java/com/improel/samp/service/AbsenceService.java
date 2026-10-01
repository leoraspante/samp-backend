// Serviço responsável pelo registro e consulta de ausências, faltas e atestados dos colaboradores.

package com.improel.samp.service;

import com.improel.samp.entity.AbsenceRecord;
import com.improel.samp.entity.User;
import com.improel.samp.repository.AbsenceRecordRepository;
import com.improel.samp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AbsenceService {

    private final AbsenceRecordRepository absenceRecordRepository;
    private final UserRepository userRepository;

    @Autowired
    public AbsenceService(AbsenceRecordRepository absenceRecordRepository, UserRepository userRepository) {
        this.absenceRecordRepository = absenceRecordRepository;
        this.userRepository = userRepository;
    }

    // Lista todos os registros de ausência.
    @Transactional(readOnly = true)
    public List<AbsenceRecord> findAll() {
        return absenceRecordRepository.findAll();
    }

    // Lista o histórico de ausências de um operador específico.
    @Transactional(readOnly = true)
    public List<AbsenceRecord> findByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado para o ID informado: " + userId));
        return absenceRecordRepository.findByUser(user);
    }

    // Registra uma nova ausência/atestado para o colaborador.
    @Transactional
    public AbsenceRecord save(AbsenceRecord absenceRecord) {
        if (absenceRecord.getStartDate() != null && absenceRecord.getEndDate() != null) {
            if (absenceRecord.getEndDate().isBefore(absenceRecord.getStartDate())) {
                throw new IllegalArgumentException("A data final da ausência não pode ser anterior a data inicial.");
            }
        }
        return absenceRecordRepository.save(absenceRecord);
    }
}

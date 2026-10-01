// Interface de acesso a dados para a entidade AbsenceRecord.

package com.improel.samp.repository;

import com.improel.samp.entity.AbsenceRecord;
import com.improel.samp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface AbsenceRecordRepository extends JpaRepository<AbsenceRecord, Long> {

    // Lista todas as ausências de um determinado colaborador.
    List<AbsenceRecord> findByUser(User user);

    // Lista a ausência de um colaborador dentro de um período (intervalo de datas).
    List<AbsenceRecord> findByUserAndStartDateBetween(User user, LocalDate startDate, LocalDate endDate);

    // Lista todas as ausências registradas em uma data específica.
    List<AbsenceRecord> findByStartDate(LocalDate startDate);
}

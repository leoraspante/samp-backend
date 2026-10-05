// Interface de acesso a dados para o registro de jornadas.

package com.improel.samp.repository;

import com.improel.samp.entity.ShiftRecord;
import com.improel.samp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ShiftRecordRepository extends JpaRepository<ShiftRecord, Long> {

    // Busca o turno atual em aberto do operador (sem horário de saída).
    Optional<ShiftRecord> findByOperatorAndLogoutTimeIsNull(User operator);
}

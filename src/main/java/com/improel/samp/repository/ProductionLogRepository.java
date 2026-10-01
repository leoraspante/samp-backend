// Interface de acesso a dados para a entidade ProductionLog.

package com.improel.samp.repository;

import com.improel.samp.entity.ProductionLog;
import com.improel.samp.entity.ProductionOrder;
import com.improel.samp.entity.User;
import com.improel.samp.entity.Workstation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductionLogRepository extends JpaRepository<ProductionLog, Long> {

    // Lista o histórico de logs de uma determinada Ordem de Produção em ordem cronológica.
    List<ProductionLog> findByProductionOrderOrderByStartTimestampAsc (ProductionOrder productionOrder);

    // Lista os logs registrados em uma bancada de montagem específica.
    List<ProductionLog> findByWorkstation (Workstation workstation);

    // Lista os apontamentos efetuados por um determinado operador.
    List<ProductionLog> findByOperator (User operator);

    // Busca o último evento/log registrado em uma bancada específica.
    Optional<ProductionLog> findFirstByWorkstationOrderByStartTimestampDesc (Workstation workstation);

    // Busca o último evento registrado em uma Ordem de Produção específica.
    Optional<ProductionLog> findFirstByProductionOrderOrderByStartTimestampDesc  (ProductionOrder productionOrder);
}

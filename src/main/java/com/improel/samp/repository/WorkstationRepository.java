// Interface de acesso a dados para a entidade Workstation.

package com.improel.samp.repository;

import com.improel.samp.entity.Workstation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface WorkstationRepository extends JpaRepository<Workstation, Long> {

    // Busca uma bancada pelo seu código identificador (ex: "ST-01").
    Optional<Workstation> findByWorkstationCode(String workstationCode);

    // Busca a bancada vinculada ao UUID do tablet da bancada.
    Optional<Workstation> findByTabletUuid(String tabletUuid);

    // Verifica se já existe uma bancada cadastrada com o código informado.
    boolean existsByWorkstationCode(String workstationCode);
}

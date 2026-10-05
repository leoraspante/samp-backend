// Interface de acesso a dados para a entidade Workstation.

package com.improel.samp.repository;

import com.improel.samp.entity.Workstation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface WorkstationRepository extends JpaRepository<Workstation, Long> {

    // Busca uma bancada pelo seu código nome (ex: "ST-01").
    Optional<Workstation> findByName(String name);

    // Busca a bancada vinculada ao UUID do tablet da bancada.
    Optional<Workstation> findByTabletUuid(String tabletUuid);

    // Verifica se já existe uma bancada cadastrada com o nome informado.
    boolean existsByName(String name);

    Long id(Long id);
}

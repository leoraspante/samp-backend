// Interface de acesso a dados para a entidade User.

package com.improel.samp.repository;

import com.improel.samp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Busca colaborador pelo PIN de acesso (usado no login rápido do tablet).
    Optional<User> findByPinCode(String pinCode);

    // Busca colaborador pela matrícula funcional.
    Optional<User> findByRegistrationNumber(String registrationNumber);

    // Busca usuário pelo e-mail (usado no login do gestor/sistema web).
    Optional<User> findByEmail(String email);

    // Verificações para evitar duplicidade no cadastro.
    boolean existsByPinCode(String pinCode);
    boolean existsByRegistrationNumber(String registrationNumber);
    boolean existsByEmail(String email);
}

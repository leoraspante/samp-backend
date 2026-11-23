// Serviço responsável pela lógica de negócio e autenticação de usuários/operadores.

package com.improel.samp.service;

import com.improel.samp.dto.request.CreateUserRequestDTO;
import com.improel.samp.entity.User;
import com.improel.samp.exception.InvalidCredentialsException;
import com.improel.samp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // Busca todos os colaboradores cadastrados.
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    // Busca um colaborador pelo ID.
    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Autentica o operador no tablet através do PIN de acesso rápido.
    @Transactional(readOnly = true)
    public User authenticateByPin(String pinCode) {
        return userRepository.findByPinCode(pinCode)
                .filter(user -> Boolean.TRUE.equals(user.getActive()))
                .orElseThrow(() -> new InvalidCredentialsException("PIN invalido ou usuario inativo"));
    }

    // Busca colaborador pela matrícula funcional.
    @Transactional(readOnly = true)
    public Optional<User> findByRegistrationNumber(String registrationNumber) {
        return userRepository.findByRegistrationNumber(registrationNumber);
    }

    // Cria um novo usuário a partir do DTO, garantindo que não haja duplicidade de PIN, Matrícula ou E-mail.
    @Transactional
    public User create(CreateUserRequestDTO request) {
        if (userRepository.existsByPinCode(request.pinCode())) {
            throw new IllegalArgumentException("Já existe um colaborador cadastrado com esse PIN.");
        }
        if (userRepository.existsByRegistrationNumber(request.registrationNumber())) {
            throw new IllegalArgumentException("Já existe um colaborador cadastrado com esta matrícula.");
        }
        if (request.email() != null && !request.email().isBlank() && userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Já existe um colaborador cadastrado com este e-mail.");
        }

        User user = new User(
                null,
                request.fullName(),
                request.registrationNumber(),
                request.email(),
                request.pinCode(),
                request.role()
        );

        return userRepository.save(user);
    }
}

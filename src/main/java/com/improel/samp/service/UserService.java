// Serviço responsável pela lógica de negócio e autenticação de usuários/operadores.

package com.improel.samp.service;

import com.improel.samp.entity.User;
import com.improel.samp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Busca todos os colaboradores cadastrados.
    @Transactional(readOnly = true)
    public List<User> findAll(){
        return userRepository.findAll();
    }

    // Busca um colaborador pelo ID.
    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id){
        return userRepository.findById(id);
    }

    // Autentica o operador no tablet através do PIN de acesso rápido.
    @Transactional(readOnly = true)
    public User authenticateByPin(String pinCode) {
        return userRepository.findByPinCode(pinCode)
                .filter(User::getActive)
                .orElseThrow(() -> new IllegalArgumentException("PIN invalido ou usuario inativo"));
    }

    // Busca colaborador pela matrícula funcional.
    @Transactional(readOnly = true)
    public Optional<User> findByRegistrationNumber(String registrationNumber){
        return userRepository.findByRegistrationNumber(registrationNumber);
    }

    // Salva ou atualiza um usuário, garantindo que não haja duplicidade de PIN, Matrícula ou E-mail.
    @Transactional
    public User save(User user){
        if (user.getId() == null){
            if(user.getPinCode() != null && userRepository.existsByPinCode(user.getPinCode())) {
                throw new IllegalArgumentException("Já existe um colaborador cadastrado com esse PIN.");
            }
            if(user.getRegistrationNumber() != null && userRepository.existsByRegistrationNumber(user.getRegistrationNumber())) {
                throw new IllegalArgumentException("Já existe um colaborador cadastrado com esta matrícula.");
            }
            if(user.getEmail() != null && userRepository.existsByEmail(user.getEmail())) {
                throw new IllegalArgumentException("Já existe um colaborador cadastrado com este e-mail.");
            }
        }
        return userRepository.save(user);
    }
}

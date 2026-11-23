// Controller responsável pelas requisições referentes aos usuários.

package com.improel.samp.controller;

import com.improel.samp.dto.request.CreateUserRequestDTO;
import com.improel.samp.dto.request.PinAuthRequestDTO;
import com.improel.samp.dto.response.UserResponseDTO;
import com.improel.samp.entity.User;
import com.improel.samp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 1. Lista todos os usuários.
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findAll() {
        List<UserResponseDTO> users = userService.findAll().stream().map(UserResponseDTO::fromEntity).toList();
        return ResponseEntity.ok(users);
    }

    // 2. Busca um usuário pelo ID.
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(UserResponseDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. Cadastra um novo usuário (Painel de Controle do Gestor).
    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@Valid @RequestBody CreateUserRequestDTO request) {
        User savedUser = userService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponseDTO.fromEntity(savedUser));
    }

    // 4. Autenticação rápida por PIN (Utilizado pelo Tablet).
    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> loginByPin(@RequestBody PinAuthRequestDTO request) {
        User user = userService.authenticateByPin(request.pinCode());
        return ResponseEntity.ok(UserResponseDTO.fromEntity(user));
    }
}

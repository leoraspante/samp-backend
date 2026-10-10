// Controller responsável pelas requisições referentes aos usuários.

package com.improel.samp.controller;

import com.improel.samp.dto.PinAuthRequest;
import com.improel.samp.entity.User;
import com.improel.samp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 1. Lista todos os usuários.
    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    // 2. Busca um usuário pelo ID.
    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. Cadastra um novo usuário (Painel de Controle do Gestor).
    @PostMapping
    public ResponseEntity<?> create(@RequestBody User user) {
        try {
            User savedUser = userService.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());    // Retorna 400 Bad Request se Matrícula/PIN/Email já existirem.
        }
    }

    // 4. Autenticação rápida por PIN (Utilizado pelo Tablet).
    // POST: http://localhost:8080/api/users/login
    @PostMapping("/login")
    public ResponseEntity<?> loginByPin(@RequestBody PinAuthRequest request) {
        try{
            User user = userService.authenticateByPin(request.pinCode());
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            // Retorna 401 Unauthorized se o PIN for inválido.
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}

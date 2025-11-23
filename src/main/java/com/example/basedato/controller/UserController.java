package com.example.basedato.controller;

import com.example.basedato.model.User;
import com.example.basedato.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users") // Ruta usuarios
public class UserController {

    @Autowired
    private UserService userService;

    // 1. Obtener todos los usuarios
    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    // 2. Obtener usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 3. Editar usuario
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        Optional<User> userOptional = userService.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Solo actualizamos campos permitidos (no password ni rol por aquí por seguridad básica)
            user.setNombre(userDetails.getNombre());
            user.setApellido(userDetails.getApellido());
            user.setEmail(userDetails.getEmail());

            return ResponseEntity.ok(userService.update(user));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 4. Eliminar usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
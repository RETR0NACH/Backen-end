package com.example.basedato.controller;

import com.example.basedato.model.User;
import com.example.basedato.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private UserService userService;

    // 1. Registro de Usuario (POST /api/v1/auth/register)
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user){
        if (userService.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body("Error: El email ya está en uso.");
        }
        // Por defecto, si no traen rol, los dejamos como 'cliente'
        if (user.getRol() == null || user.getRol().isEmpty()) {
            user.setRol("cliente");
        }

        // Guardar usuario
        User newUser = userService.saveUser(user);
        return ResponseEntity.ok("Usuario registrado exitosamente con ID: " + newUser.getId());
    }

    // 2. Login de Usuario (POST /api/v1/auth/login)
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User loginRequest) {
        Optional<User> user = userService.getUserByEmail(loginRequest.getEmail());

        if (user.isPresent()) {
            // Verificación de contraseña simple (Temporal hasta implementar JWT)
            if (user.get().getPassword().equals(loginRequest.getPassword())) {
                return ResponseEntity.ok("Login exitoso (Token JWT pendiente de implementar)");
            }
        }
        return ResponseEntity.status(401).body("Error: Credenciales incorrectas");
    }
}
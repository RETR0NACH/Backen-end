package com.example.basedato.auth;

import com.example.basedato.model.User;
import com.example.basedato.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    // Registro se mantiene casi igual, pero sin generar token
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        try {
            User newUser = new User();
            newUser.setNombre(request.getNombre());
            newUser.setApellido(request.getApellido());
            newUser.setEmail(request.getEmail());
            newUser.setPassword(request.getPassword());
            newUser.setRol(request.getRol());

            User savedUser = userService.registerUser(newUser);

            // Devolvemos el usuario creado sin token
            return ResponseEntity.ok(AuthResponse.builder()
                    .id(savedUser.getId())
                    .email(savedUser.getEmail())
                    .rol(savedUser.getRol())
                    .token("BASIC") // Dummy text
                    .build());

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody AuthRequest loginRequest) {
        try {
            // Intentamos autenticar
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            User userDetails = (User) authentication.getPrincipal();

            return ResponseEntity.ok(AuthResponse.builder()
                    .id(userDetails.getId())
                    .email(userDetails.getEmail())
                    .rol(userDetails.getRol())
                    .token("BASIC_AUTH")
                    .build());

        } catch (Exception e) {
            // Si falla (contraseña mal, usuario no existe), devolvemos 401 CLARO
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas: " + e.getMessage());
        }
    }
}
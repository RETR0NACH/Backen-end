package com.example.basedato.auth;

import com.example.basedato.model.User;
import com.example.basedato.security.jwt.JwtTokenProvider;
import com.example.basedato.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

// Crea los DTOs: AuthRequest (email, password) y AuthResponse (token, id, email, rol)

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    // Endpoint de registro
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        try {
            // Convertimos manualmente
            User newUser = new User();
            newUser.setNombre(request.getNombre());
            newUser.setApellido(request.getApellido());
            newUser.setEmail(request.getEmail());
            newUser.setPassword(request.getPassword());

            User savedUser = userService.registerUser(newUser);
            String token = tokenProvider.generateToken(savedUser);

            return ResponseEntity.ok(AuthResponse.builder()
                    .token(token)
                    .id(savedUser.getId())
                    .email(savedUser.getEmail())
                    .rol(savedUser.getRol())
                    .build());

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Endpoint de login
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody AuthRequest loginRequest) {
        // 1. Autenticar usando Spring Security (lanza excepción si falla)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        // 2. Establecer la autenticación en el contexto de seguridad
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Generar el token JWT
        User userDetails = (User) authentication.getPrincipal(); // Castea a tu clase User
        String token = tokenProvider.generateToken(userDetails);

        // 4. Retornar la respuesta con el token y datos del usuario
        return ResponseEntity.ok(AuthResponse.builder()
                .token(token)
                .id(userDetails.getId())
                .email(userDetails.getEmail())
                .rol(userDetails.getRol())
                .build());
    }
}
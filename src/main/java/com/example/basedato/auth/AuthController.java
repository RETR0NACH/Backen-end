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

            User savedUser = userService.registerUser(newUser);

            // Devolvemos el usuario creado sin token
            return ResponseEntity.ok(AuthResponse.builder()
                    .id(savedUser.getId())
                    .email(savedUser.getEmail())
                    .rol(savedUser.getRol())
                    .token("BASIC_AUTH") // Valor dummy para no romper tu frontend si espera un string
                    .build());

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody AuthRequest loginRequest) {
        // Esto verifica si el usuario y contraseña son reales en la BD
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        User userDetails = (User) authentication.getPrincipal();

        // Retornamos los datos del usuario.
        // NOTA: Ya no enviamos un token real, el frontend guardará la contraseña encriptada en base64.
        return ResponseEntity.ok(AuthResponse.builder()
                .id(userDetails.getId())
                .email(userDetails.getEmail())
                .rol(userDetails.getRol())
                .token("BASIC_AUTH")
                .build());
    }
}
package com.example.basedato.auth; // Mismo paquete que AuthController

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String token;
    private Long id;
    private String email;
    private String rol;
}
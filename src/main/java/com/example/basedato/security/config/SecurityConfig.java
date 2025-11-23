package com.example.basedato.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; // Importante
import org.springframework.security.config.http.SessionCreationPolicy; // Importante
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. Desactivar CSRF (Vital para APIs REST)
                .csrf(csrf -> csrf.disable())

                // 2. Activar CORS desde la capa de seguridad
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 3. Configurar sesión como STATELESS (No guardar cookies)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 4. Configurar rutas públicas (OJO con el /api/v1)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/**").permitAll()     // Login y Registro
                        .requestMatchers("/api/v1/products/**").permitAll() // Ver productos
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll() // Swagger
                        .anyRequest().authenticated() // El resto requiere token
                );

        return http.build();
    }

    // Configuración CORS explícita para Spring Security
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Permite tu frontend en Vercel y Localhost
        configuration.setAllowedOrigins(Arrays.asList("https://andromeda-s-inn-shop-lg6n.vercel.app", "http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
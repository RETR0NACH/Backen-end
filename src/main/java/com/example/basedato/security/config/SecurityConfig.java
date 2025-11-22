package com.example.basedato.security.config;

// ... (Las importaciones se mantienen)

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// Importaciones para CORS (mantener)
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import com.example.basedato.security.jwt.JwtAuthenticationFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter, AuthenticationProvider authenticationProvider) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. DESHABILITAR CSRF (Sintaxis moderna)
                .csrf(csrf -> csrf.disable())

                // 2. APLICAR CORS (Sintaxis moderna, usando el bean corsConfigurationSource)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 3. DEFINIR REGLAS DE AUTORIZACIÓN
                .authorizeHttpRequests(auth -> auth
                        // Rutas públicas: Auth, Swagger
                        .requestMatchers("/api/v1/auth/**", "/v2/api-docs", "/v3/api-docs/**",
                                "/swagger-resources", "/swagger-resources/**",
                                "/configuration/ui", "/configuration/security",
                                "/swagger-ui.html", "/swagger-ui/**", "/webjars/**")
                        .permitAll()

                        // Rutas de la tienda: Permite a todos ver los productos
                        .requestMatchers("/api/v1/products/**").permitAll() //

                        // Rutas protegidas por rol (ejemplo)
                        .requestMatchers("/api/v1/admin/**").hasAuthority("ADMIN")

                        // Cualquier otra petición requiere autenticación
                        .anyRequest().authenticated()
                )

                // 4. CONFIGURAR GESTIÓN DE SESIONES
                .sessionManagement(session -> session
                        // Importante: No usar sesiones de estado (STATELWESS)
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 5. AGREGAR FILTRO JWT Y PROVEEDOR DE AUTENTICACIÓN
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        // Ya no necesitas el .and() explícito,x la cadena se construye con los lambdas
        return http.build();
    }

    // El Bean de CORS se mantiene igual
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:5173",
                "http://localhost:3000",
                "http://localhost:8080",
                "https://andromeda-s-inn-shop-lg6n.vercel.app"
        ));

        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
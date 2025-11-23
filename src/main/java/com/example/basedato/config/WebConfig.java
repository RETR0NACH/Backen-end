package com.example.basedato.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
public class WebConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // 1. Permitir credenciales (cookies, auth headers)
        config.setAllowCredentials(true);

        // 2. Dominios permitidos (Pon aquí SOLO los que uses)
        config.setAllowedOrigins(Arrays.asList(
                "https://andromeda-s-inn-shop-3eva.vercel.app",
                "http://localhost:5173",
                "http://localhost:3000"
        ));

        // 3. Headers y Métodos permitidos
        config.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // 4. Registrar la configuración para todas las rutas
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
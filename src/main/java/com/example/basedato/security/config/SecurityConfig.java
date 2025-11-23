package com.example.basedato.security.config;

import com.example.basedato.security.jwt.JwtAuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

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
                // 1. Deshabilitar CSRF
                .csrf(csrf -> csrf.disable())

                // NOTA: Hemos quitado .cors() de aquí para usar el filtro de alta prioridad abajo

                // 2. Reglas de Autorización
                .authorizeHttpRequests(auth -> auth
                        // Rutas públicas (Swagger, Auth)
                        .requestMatchers("/api/v1/auth/**", "/v2/api-docs", "/v3/api-docs/**",
                                "/swagger-resources/**", "/configuration/**", "/swagger-ui/**",
                                "/webjars/**", "/swagger-ui.html").permitAll()

                        // Catálogo público
                        .requestMatchers("/api/v1/products/**").permitAll()

                        // Rutas de Admin
                        .requestMatchers("/api/v1/admin/**").hasAuthority("ADMIN")

                        // Todo lo demás requiere autenticación
                        .anyRequest().authenticated()
                )

                // 3. Gestión de Sesión (Stateless)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 4. Filtro JWT
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // --- SOLUCIÓN DEFINITIVA PARA CORS ---
    // Este filtro se ejecuta ANTES que la seguridad de Spring.
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // 1. Permitir credenciales (Tokens/Cookies)
        config.setAllowCredentials(true);

        // 2. Dominios permitidos (Asegúrate de que tu URL de Vercel esté bien escrita aquí)
        config.setAllowedOrigins(Arrays.asList(
                "http://localhost:5173",
                "http://localhost:3000",
                "http://localhost:8080",
                "https://andromeda-s-inn-shop-lg6n.vercel.app" // <--- Tu URL de Vercel
        ));

        // 3. Headers permitidos (Todo)
        config.setAllowedHeaders(Arrays.asList(
                "Origin", "Content-Type", "Accept", "Authorization",
                "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"
        ));

        // 4. Métodos permitidos (Todo)
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

        source.registerCorsConfiguration("/**", config);

        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        // Esto le da la prioridad máxima: Se ejecuta primero que todo
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }
}
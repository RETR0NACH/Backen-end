package com.example.basedato.config;

import com.example.basedato.model.User;
import com.example.basedato.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Verificar si ya existe el admin para no duplicarlo
            if (userRepository.findByEmail("andromeda@growshop.cl").isEmpty()) {
                User admin = new User();
                admin.setNombre("Andromeda");
                admin.setApellido("Admin");
                admin.setEmail("andromeda@growshop.cl");
                // Aquí encriptamos la contraseña automáticamente
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRol("admin"); // Rol exacto que espera tu frontend

                userRepository.save(admin);
                System.out.println("Usuario Admin creado: andromeda@growshop.cl / admin123");
            } else {
                System.out.println("El usuario Admin ya existe.");
            }
        };
    }
}
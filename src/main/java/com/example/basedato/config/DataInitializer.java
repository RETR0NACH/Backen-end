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
            // CAMBIAMOS EL CORREO PARA FORZAR LA CREACIÓN DE UN NUEVO ADMIN LIMPIO
            String adminEmail = "superadmin@growshop.cl"; // <--- CAMBIO AQUÍ

            if (userRepository.findByEmail(adminEmail).isEmpty()) {
                User admin = new User();
                admin.setNombre("Super");
                admin.setApellido("Admin");
                admin.setEmail(adminEmail);
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRol("admin");

                userRepository.save(admin);
                System.out.println(" NUEVO ADMIN CREADO: " + adminEmail);
            }
        };
    }
}
package com.example.basedato.config;

import com.example.basedato.model.User;
import com.example.basedato.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String email = "andromeda@growshop.cl"; // Tu correo de admin original
            String password = "admin123";

            Optional<User> existingAdmin = userRepository.findByEmail(email);

            if (existingAdmin.isPresent()) {
                // SI YA EXISTE: Forzamos la actualización de la contraseña
                User admin = existingAdmin.get();
                admin.setPassword(passwordEncoder.encode(password));
                admin.setRol("admin");
                userRepository.save(admin);
                System.out.println("✅ ADMIN RECUPERADO: Contraseña actualizada para " + email);
            } else {
                // SI NO EXISTE: Lo creamos desde cero
                User admin = new User();
                admin.setNombre("Andromeda");
                admin.setApellido("Admin");
                admin.setEmail(email);
                admin.setPassword(passwordEncoder.encode(password));
                admin.setRol("admin");
                userRepository.save(admin);
                System.out.println("✅ ADMIN CREADO: " + email);
            }
        };
    }
}
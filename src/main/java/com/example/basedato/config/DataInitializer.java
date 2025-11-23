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
            String email = "superadmin@growshop.cl";
            String pass = "admin123";

            Optional<User> existingAdmin = userRepository.findByEmail(email);

            if (existingAdmin.isEmpty()) {
                // Crear si no existe
                User admin = new User();
                admin.setNombre("Super");
                admin.setApellido("Admin");
                admin.setEmail(email);
                admin.setPassword(passwordEncoder.encode(pass));
                admin.setRol("admin");
                userRepository.save(admin);
                System.out.println("✅ ADMIN CREADO: " + email);
            } else {
                // SI EXISTE, ACTUALIZAMOS LA CONTRASEÑA PARA ASEGURARNOS QUE FUNCIONE
                User admin = existingAdmin.get();
                admin.setPassword(passwordEncoder.encode(pass));
                admin.setRol("admin"); // Aseguramos el rol
                userRepository.save(admin);
                System.out.println("🔄 ADMIN ACTUALIZADO: " + email);
            }
        };
    }
}
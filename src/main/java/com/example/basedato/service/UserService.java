package com.example.basedato.service;

import com.example.basedato.model.User;
import com.example.basedato.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // Importación necesaria
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // <-- Inyección del codificador

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    // Lógica para registrar un nuevo usuario con cifrado de contraseña
    public User registerUser(User user) {
        if (findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }

        // Cifrar la contraseña antes de guardar
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRol("CLIENTE"); // Asignar el rol CLILENTE por defecto

        return userRepository.save(user);
    }

    public User update(User user) {
        return userRepository.save(user);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
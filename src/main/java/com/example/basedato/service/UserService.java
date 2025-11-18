package com.example.basedato.service;

import com.example.basedato.model.User;
import com.example.basedato.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Obtener todos los usuarios (útil para el panel de admin)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Obtener un usuario por su ID
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Obtener un usuario por su EMAIL (¡Esto es vital para el Login!)
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Guardar o actualizar un usuario (para Registro o Edición)
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Eliminar un usuario
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // Validar si existe un email (útil para no crear duplicados al registrarse)
    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
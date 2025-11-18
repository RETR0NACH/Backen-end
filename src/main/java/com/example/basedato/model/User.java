package com.example.basedato.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Data
@Table(name = "usuarios")
public class User implements UserDetails { // IMPLEMENTAR UserDetails
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellido;

    @Column(unique = true)
    private String email;

    private String password;
    private String rol; // "admin" o "cliente"

    // --- Métodos de UserDetails (requeridos para Spring Security) ---

    // Retorna los permisos/roles del usuario
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Se asume que el campo 'rol' contiene el rol (ej: "ADMIN", "CLIENTE")
        return List.of(new SimpleGrantedAuthority(rol.toUpperCase()));
    }

    // Retorna el email (usado como 'username' en Spring Security)
    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
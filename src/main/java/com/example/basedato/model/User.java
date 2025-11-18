package com.example.basedato.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "usuarios")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellido;

    @Column(unique = true) // El email no se puede repetir
    private String email;

    private String password;
    private String rol; // "admin" o "cliente"
}

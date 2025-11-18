package com.example.basedato.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data // Lombok crea getters, setters y toString automáticamente
@Table(name = "productos")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private Integer precio;
    private String img;
    private String categoria;

    @Column(length = 1000) // Permitimos descripciones largas
    private String descripcion;
}
